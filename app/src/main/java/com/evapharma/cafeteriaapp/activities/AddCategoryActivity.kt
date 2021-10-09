package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.evapharma.cafeteriaapp.IMG_HEIGHT
import com.evapharma.cafeteriaapp.IMG_WIDTH
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.api.SessionManager
import com.evapharma.cafeteriaapp.databinding.ActivityAddCategoryBinding
import com.evapharma.cafeteriaapp.functions.isValidUrl
import com.evapharma.cafeteriaapp.models.CategoryRequest
import com.evapharma.cafeteriaapp.models.CategoryResponse
import com.evapharma.cafeteriaapp.services.CategoryService
import id.ionbit.ionalert.IonAlert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddCategoryBinding
    //to show or hide loading:
    private lateinit var loadingDialog : IonAlert

    private val SELECT_PICTURE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = IonAlert(this@AddCategoryActivity, IonAlert.PROGRESS_TYPE)
            .setSpinColor("#053776").setSpinKit("ThreeBounce")

        initButtons()
        initEt()
    }

    private fun initEt(){
        binding.etAddcatCatimgurl.doOnTextChanged { text, start, before, count ->
            Glide.with(this)
                .load(text.toString())
                .placeholder(R.drawable.im_meal)
                .error(R.drawable.ic_error_sign)
                .override(IMG_WIDTH, IMG_HEIGHT)
                .centerCrop()
                .into(binding.ivAddcatCatimg)
        }
    }

    private fun validateForm(){
        binding.etAddcatCatimgurl.apply {
            if(!isValidUrl(text.toString())){
                error="Invalid Url"
            }else error=null
        }
        binding.etAddcatCatname.apply{
            if(text.toString().isEmpty()){
                error="Can't be empty"
            }else error=null
        }


        }

    private fun isValid():Boolean{
        return binding.etAddcatCatname.text.toString().isNotEmpty() &&
                isValidUrl(binding.etAddcatCatimgurl.text.toString())
    }


    private fun initButtons(){
        binding.btnAddcatAdd.setOnClickListener {
            validateForm()
            if(isValid()){
                addNewCategoryAPI()
            }
        }
        binding.imgAddmealUpcatimg.setOnClickListener {
            imageChooser()
        }
    }

    private fun imageChooser() {
        // create an instance of the
        // intent of the type image
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        // pass the constant to compare it with the returned requestCode
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
                    binding.ivAddcatCatimg.setImageResource(0)
                    binding.ivAddcatCatimg.setImageURI(selectedImageUri)
                }
            }
        }
    }


    /**Add new category API*/
    private fun addNewCategoryAPI(){
        loadingDialog.show()
        val sendCategory = CategoryRequest()
        sendCategory.imageUrl = binding.etAddcatCatimgurl.text.toString()
        sendCategory.name = binding.etAddcatCatname.text.toString()

        val categoryService: CategoryService = ApiClient(this@AddCategoryActivity).buildService(CategoryService::class.java)
        val requestCall : Call<CategoryResponse> = categoryService.createCategory(sendCategory)
        requestCall.enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if(response.isSuccessful){
                    loadingDialog.dismiss()
                    IonAlert(this@AddCategoryActivity, IonAlert.SUCCESS_TYPE)
                        .setTitleText("ADDED")
                        .setContentText("Current category added successfully.")
                        .setConfirmClickListener {
                            finish()
                        }
                        .show()
                }else{
                    loadingDialog.dismiss()
                    val errorCode:Any = when(response.code()){
                        401 -> {
                            IonAlert(this@AddCategoryActivity, IonAlert.ERROR_TYPE)
                                .setTitleText("ERROR")
                                .setContentText("401 unauthorized user, please login.")
                                .setConfirmClickListener {
                                    SessionManager(this@AddCategoryActivity).deleteAccessToken()
                                    it.hide()
                                    finishAffinity()
                                    startActivity(Intent(this@AddCategoryActivity, LoginActivity::class.java))
                                    Animatoo.animateSplit(this@AddCategoryActivity)
                                }
                                .show()
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
                    IonAlert(this@AddCategoryActivity, IonAlert.ERROR_TYPE)
                        .setTitleText("ERROR")
                        .setContentText(errorCode.toString())
                        .show()
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                loadingDialog.dismiss()
                IonAlert(this@AddCategoryActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("ERROR")
                    .setContentText("Something went wrong, $t")
                    .show()
            }

        })
    }
}

