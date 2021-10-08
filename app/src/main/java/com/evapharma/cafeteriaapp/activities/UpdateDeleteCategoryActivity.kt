package com.evapharma.cafeteriaapp.activities
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.evapharma.cafeteriaapp.*
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.api.SessionManager
import com.evapharma.cafeteriaapp.databinding.ActivityUpdateDeleteCategoryBinding
import com.evapharma.cafeteriaapp.models.CategoryRequest
import com.evapharma.cafeteriaapp.models.CategoryResponse
import com.evapharma.cafeteriaapp.services.CategoryService
import id.ionbit.ionalert.IonAlert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpdateDeleteCategoryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUpdateDeleteCategoryBinding

    //to show or hide loading:
    private lateinit var loadingDialog : IonAlert

    //Current category
    private lateinit var currentCatResponse: CategoryResponse
    private lateinit var categoryService:CategoryService

    private val SELECT_PICTURE = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateDeleteCategoryBinding.inflate(layoutInflater)

        loadingDialog = IonAlert(this@UpdateDeleteCategoryActivity, IonAlert.PROGRESS_TYPE)
            .setSpinColor("#053776").setSpinKit("ThreeBounce")

        categoryService = ApiClient(this@UpdateDeleteCategoryActivity).buildService(CategoryService::class.java)

        setContentView(binding.root)
        initBtnUploadImg()
        initBtnsClick()
        initEt()
        loadCurrentCatData()
    }

    private fun initBtnsClick(){
        binding.btnUdcatUpdate.setOnClickListener {
            validateForm()
            if(isValid()){
                updateAPI()
            }
        }
        binding.btnUdcatDelete.setOnClickListener {
            IonAlert(this@UpdateDeleteCategoryActivity, IonAlert.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this category!")
                .setConfirmText("Yes")
                .setCancelText("Cancel")
                .setConfirmClickListener {
                    it.hide()
                    deleteAPI()
                }
                .show()
        }
    }

    private fun initBtnUploadImg(){
        binding.imgUdcatUpcatimg.setOnClickListener {
            imageChooser()
        }
        binding.btnUdcatUpdate.setOnClickListener {
            updateAPI()
        }
        binding.btnUdcatDelete.setOnClickListener {
            IonAlert(this@UpdateDeleteCategoryActivity, IonAlert.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this item!")
                .setConfirmText("Delete")
                .setCancelText("Cancel")
                .setConfirmClickListener {
                    it.hide()
                    deleteAPI()
                }
                .show()
        }
    }

    private fun initEt(){
        binding.etUdcatCatimgurl.doOnTextChanged { text, start, before, count ->
            Glide.with(this)
                .load(text.toString())
                .placeholder(R.drawable.ic_meal)
                .error(R.drawable.ic_error_sign)
                .override(IMG_WIDTH, IMG_HEIGHT)
                .centerCrop()
                .into(binding.ivUdcatCatimg)
        }
    }

    private fun validateForm(){
        binding.etUdcatCatimgurl.also {
            if(!isValidUrl(it.text.toString())){
                it.error="Invalid Url"
            }else it.error=null
        }
        binding.etUdcatCatname.also{
            if(it.text.toString().isEmpty()){
                it.error="Can't be empty"
            }else it.error=null
        }

    }

    private fun isValid():Boolean{
        return binding.etUdcatCatname.text.toString().isNotEmpty() &&
                isValidUrl(binding.etUdcatCatimgurl.text.toString())
    }

    private fun imageChooser() {
        // create an instance of the
        // intent of the type image
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                val selectedImageUri: Uri? = data!!.data
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    binding.ivUdcatCatimg.setImageResource(0)
                    binding.ivUdcatCatimg.setImageURI(selectedImageUri)
                }
            }
        }
    }

    //Get current category data from last page:
    private fun loadCurrentCatData(){
        val bundle:Bundle? = intent.extras
        if(bundle?.containsKey(CATEGORY_DATA)!!){
            currentCatResponse = intent.extras?.get(CATEGORY_DATA) as CategoryResponse
        }
        binding.etUdcatCatimgurl.setText(currentCatResponse.imageUrl)
        binding.etUdcatCatname.setText(currentCatResponse.name)
    }

    /**Call api to update*/
    private fun updateAPI(){
        loadingDialog.show()
        val updateCatRequest = CategoryRequest(
            binding.etUdcatCatimgurl.text.toString(),
            binding.etUdcatCatname.text.toString(),
        )
        val requestCall: Call<CategoryResponse> = categoryService.updateCategory(
            currentCatResponse.id!!,
            updateCatRequest
        )
        requestCall.enqueue(object :Callback<CategoryResponse>{
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if(response.isSuccessful){
                    loadingDialog.dismiss()
                    IonAlert(this@UpdateDeleteCategoryActivity, IonAlert.SUCCESS_TYPE)
                        .setTitleText("UPDATED")
                        .setContentText("Current category updated successfully.")
                        .setConfirmClickListener {
                            finish()
                        }
                        .show()
                }else{
                    loadingDialog.dismiss()
                    val errorCode:Any= when(response.code()){
                        401 -> {
                            logoutUser()
                            return
                        }
                        404 -> {
                            "404 not found"
                        }
                        500 -> {
                            "500 server broken"
                        }
                        else ->{
                            "Unknown error!"
                        }
                    }
                    IonAlert(this@UpdateDeleteCategoryActivity, IonAlert.ERROR_TYPE)
                        .setTitleText("ERROR")
                        .setContentText(errorCode.toString())
                        .show()
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                loadingDialog.dismiss()
                IonAlert(this@UpdateDeleteCategoryActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("ERROR")
                    .setContentText("Something went wrong, $t")
                    .show()
            }

        })
    }
    /**Call api to delete*/
    private fun deleteAPI(){
        loadingDialog.show()
        val requestCall : Call<Unit> = categoryService.deleteCategory(currentCatResponse.id!!)
        requestCall.enqueue(object: Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    loadingDialog.dismiss()
                    IonAlert(this@UpdateDeleteCategoryActivity, IonAlert.SUCCESS_TYPE)
                        .setTitleText("DELETED")
                        .setContentText("Current category deleted successfully.")
                        .setConfirmClickListener {
                            finish()
                        }
                        .show()
                }else{
                    loadingDialog.dismiss()
                    val errorCode:Any= when(response.code()){
                        401 -> {
                            logoutUser()
                            return
                        }
                        404 -> {
                            "404 not found"
                        }
                        500 -> {
                            "500 server broken"
                        }
                        else ->{
                            "Unknown error!"
                        }
                    }
                    IonAlert(this@UpdateDeleteCategoryActivity, IonAlert.ERROR_TYPE)
                        .setTitleText("ERROR")
                        .setContentText(errorCode.toString())
                        .show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                loadingDialog.dismiss()
                IonAlert(this@UpdateDeleteCategoryActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("ERROR")
                    .setContentText("Something went wrong, $t")
                    .show()
            }

        })
    }

    fun logoutUser(){
        IonAlert(this@UpdateDeleteCategoryActivity, IonAlert.ERROR_TYPE)
            .setTitleText("ERROR")
            .setContentText("401 unauthorized user, please login")
            .setConfirmClickListener {
                SessionManager(this@UpdateDeleteCategoryActivity).deleteAccessToken()
                it.hide()
                finishAffinity()
                startActivity(Intent(this@UpdateDeleteCategoryActivity, LoginActivity::class.java))
                Animatoo.animateSplit(this@UpdateDeleteCategoryActivity)
            }
            .show()
    }

}