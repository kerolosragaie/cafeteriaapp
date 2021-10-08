package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doOnTextChanged
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.evapharma.cafeteriaapp.*
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.api.SessionManager
import com.evapharma.cafeteriaapp.databinding.ActivityUpdateDeleteProductBinding
import com.evapharma.cafeteriaapp.models.ProductRequest
import com.evapharma.cafeteriaapp.models.ProductResponse
import com.evapharma.cafeteriaapp.services.CategoryService
import com.evapharma.cafeteriaapp.services.ProductService
import id.ionbit.ionalert.IonAlert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpdateDeleteProductActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUpdateDeleteProductBinding

    //to show or hide loading:
    private lateinit var loadingDialog : IonAlert

    //Current category
    private lateinit var currentProResponse: ProductResponse
    private lateinit var productService: ProductService

    private  var SELECT_PICTURE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateDeleteProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = IonAlert(this@UpdateDeleteProductActivity, IonAlert.PROGRESS_TYPE)
            .setSpinColor("#053776").setSpinKit("ThreeBounce")

        productService = ApiClient(this@UpdateDeleteProductActivity).buildService(ProductService::class.java)

        initEts()
        initBtns()
        initUploadimg()
        loadCurrentProData()
    }

    private fun initUploadimg(){
        binding.imgUdproductitemUpmealimg.setOnClickListener {
            imageChooser()
        }
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
                    binding.ivUdproductitemMealimg.setImageDrawable(null);
                    binding.ivUdproductitemMealimg.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun initEts() {
        binding.etUdproductitemMealimgurl.doOnTextChanged { text, start, before, count ->
            Glide.with(this)
                .load(text.toString()) // image url
                .placeholder(R.drawable.ic_meal) // any placeholder to load at start
                .error(R.drawable.ic_error_sign)  // any image in case of error
                .override(IMG_WIDTH, IMG_HEIGHT) // resizing
                .centerCrop()
                .into(binding.ivUdproductitemMealimg)  // imageview object
        }
    }

    private fun validateForm(){
        mutableListOf(
            binding.etUdproductitemMealname,
            binding.etUdproductitemMealprice,
            binding.etUdproductitemDescription
        ).forEach{
            if(it.text.toString().isEmpty())
                it.error="Can't be empty"
            else
                it.error=null
        }
        binding.etUdproductitemMealimgurl.also {
            if(!isValidUrl(it.text.toString().trim())){
                it.error="Invalid Url"
            }else{
                it.error=null
            }
        }
    }

    private fun isValid():Boolean{
        mutableListOf(
            binding.etUdproductitemMealname,
            binding.etUdproductitemMealprice,
            binding.etUdproductitemDescription
        ).forEach{
            if(it.text.toString().isEmpty())
                return false
        }

        if(!isValidUrl(binding.etUdproductitemMealimgurl.text.toString().trim()))
            return false

        return true
    }

    private fun initBtns() {
        binding.btnUdproductUpdate.setOnClickListener {
            validateForm()
            if(isValid()){
                updateAPI()
            }
        }

        binding.btnUdproductDelete.setOnClickListener {
             IonAlert(this@UpdateDeleteProductActivity, IonAlert.WARNING_TYPE)
                 .setTitleText("Are you sure?")
                 .setContentText("Won't be able to recover this product!")
                 .setConfirmText("Yes")
                 .setCancelText("Cancel")
                 .setConfirmClickListener {
                     deleteAPI()
                 }
                 .show()
        }
    }


    //Get current product data from last page:
    private fun loadCurrentProData(){
        val bundle:Bundle? = intent.extras
        if(bundle?.containsKey(PRODUCT_DATA)!!){
            currentProResponse = intent.extras?.get(PRODUCT_DATA) as ProductResponse
        }
        binding.etUdproductitemMealimgurl.setText(currentProResponse.imageUrl)
        binding.etUdproductitemDescription.setText(currentProResponse.description)
        binding.etUdproductitemMealname.setText(currentProResponse.name)
        binding.etUdproductitemMealprice.setText(currentProResponse.price.toString())
        if(!currentProResponse.inOffers!!){
            binding.switcherFoodUpdDel.setChecked(false)
        }
        Log.d("SEE",currentProResponse.toString())
    }

    /**Call api to update*/
    private fun updateAPI(){
        loadingDialog.show()
        val updateProRequest = ProductRequest(
            binding.etUdproductitemDescription.text.toString(),
            currentProResponse.categoryId,
            binding.etUdproductitemMealimgurl.text.toString(),
            binding.switcherFoodUpdDel.isChecked,
            binding.etUdproductitemMealname.text.toString(),
            binding.etUdproductitemMealprice.text.toString().toDouble()
        )
        val requestCall: Call<Unit> = productService.updateProduct(
            currentProResponse.id!!.toInt(),
            updateProRequest
        )
        requestCall.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    loadingDialog.dismiss()
                    IonAlert(this@UpdateDeleteProductActivity, IonAlert.SUCCESS_TYPE)
                        .setTitleText("UPDATED")
                        .setContentText("Current product updated successfully.")
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
                    IonAlert(this@UpdateDeleteProductActivity, IonAlert.ERROR_TYPE)
                        .setTitleText("ERROR")
                        .setContentText(errorCode.toString())
                        .show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                loadingDialog.dismiss()
                IonAlert(this@UpdateDeleteProductActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("ERROR")
                    .setContentText("Something went wrong, $t")
                    .show()
            }

        })
    }
    /**Call api to delete*/
    private fun deleteAPI(){
        loadingDialog.show()
        val requestCall : Call<Unit> = productService.deleteProduct(currentProResponse.id!!)
        requestCall.enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){
                    loadingDialog.dismiss()
                    IonAlert(this@UpdateDeleteProductActivity, IonAlert.SUCCESS_TYPE)
                        .setTitleText("DELETED")
                        .setContentText("Current product deleted successfully.")
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
                    IonAlert(this@UpdateDeleteProductActivity, IonAlert.ERROR_TYPE)
                        .setTitleText("ERROR")
                        .setContentText(errorCode.toString())
                        .show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                loadingDialog.dismiss()
                IonAlert(this@UpdateDeleteProductActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("ERROR")
                    .setContentText("Something went wrong, $t")
                    .show()
            }

        })
    }


    fun logoutUser(){
        IonAlert(this@UpdateDeleteProductActivity, IonAlert.ERROR_TYPE)
            .setTitleText("ERROR")
            .setContentText("401 unauthorized user, please login.")
            .setConfirmClickListener {
                SessionManager(this@UpdateDeleteProductActivity).deleteAccessToken()
                it.hide()
                finishAffinity()
                startActivity(Intent(this@UpdateDeleteProductActivity, LoginActivity::class.java))
                Animatoo.animateSplit(this@UpdateDeleteProductActivity)
            }
            .show()
    }


}