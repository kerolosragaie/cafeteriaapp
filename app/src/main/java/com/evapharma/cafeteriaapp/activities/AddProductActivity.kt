package com.evapharma.cafeteriaapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import android.content.Intent
import android.net.Uri
import androidx.core.widget.doOnTextChanged
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.evapharma.cafeteriaapp.CATEGORY_DATA
import com.evapharma.cafeteriaapp.*
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.api.SessionManager
import com.evapharma.cafeteriaapp.databinding.ActivityAddProductItemBinding
import com.evapharma.cafeteriaapp.models.*
import com.evapharma.cafeteriaapp.services.ProductService
import id.ionbit.ionalert.IonAlert
import retrofit2.*


class AddProductActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddProductItemBinding
    private var SELECT_PICTURE = 200

    //Current category
    private lateinit var currentCatResponse: CategoryResponse

    //to show or hide loading:
    private lateinit var loadingDialog : IonAlert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddProductItemBinding.inflate(layoutInflater)

        setContentView(binding.root)

        loadingDialog = IonAlert(this@AddProductActivity, IonAlert.PROGRESS_TYPE)
            .setSpinColor("#053776").setSpinKit("ThreeBounce")

        initEts()
        initbtnClickAdd()
        initUploadimg()
        loadCurrentCatData()
    }

    private fun initUploadimg(){
        binding.imgAddproductitemUpmealimg.setOnClickListener {
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
                    binding.ivAddproductitemMealimg.setImageDrawable(null);
                    binding.ivAddproductitemMealimg.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun validateForm(){
        mutableListOf(
            binding.etAddproductitemMealname,
            binding.etAddproductitemMealprice,
            binding.etAddproductitemDescription
        ).forEach{
            if(it.text.toString().isEmpty())
                it.error="Can't be empty"
            else
                it.error=null
        }
        binding.etAddproductitemMealimgurl.also {
            if(!isValidUrl(it.text.toString().trim())){
                it.error="Invalid Url"
            }else{
                it.error=null
            }
        }
    }

    private fun isValid():Boolean{
        mutableListOf(
            binding.etAddproductitemMealname,
            binding.etAddproductitemMealprice,
            binding.etAddproductitemDescription
        ).forEach{
            if(it.text.toString().isEmpty())
                return false
        }

        if(!isValidUrl(binding.etAddproductitemMealimgurl.text.toString().trim()))
            return false

        return true
    }

    private fun initbtnClickAdd(){
        binding.btnAddproductitemAdd.setOnClickListener {
            validateForm()
            if(isValid()){
                callAddProductApi()
            }
        }
    }

    private fun initEts() {
        binding.etAddproductitemMealimgurl.doOnTextChanged { text, start, before, count ->
            Glide.with(this)
                .load(text.toString()) // image url
                .placeholder(R.drawable.im_meal) // any placeholder to load at start
                .error(R.drawable.ic_error_sign)  // any image in case of error
                .override(IMG_WIDTH, IMG_HEIGHT) // resizing
                .centerCrop()
                .into(binding.ivAddproductitemMealimg)  // imageview object
        }
    }


    //Get current category data from last page:
    private fun loadCurrentCatData(){
        val bundle:Bundle? = intent.extras
        if(bundle?.containsKey(CATEGORY_DATA)!!){
            currentCatResponse = intent.extras?.get(CATEGORY_DATA) as CategoryResponse
        }
    }

    private fun callAddProductApi(){
        loadingDialog.show()
        val createProRequest = ProductRequest()
        createProRequest.imageUrl=binding.etAddproductitemMealimgurl.text.toString()
        createProRequest.price = binding.etAddproductitemMealprice.text.toString().toDouble()
        createProRequest.name = binding.etAddproductitemMealname.text.toString()
        createProRequest.inOffers = binding.switcherFoodUpdDel.isChecked
        createProRequest.categoryId = currentCatResponse.id!!.toInt()
        createProRequest.description = binding.etAddproductitemDescription.text.toString()


        val productService:ProductService = ApiClient(this@AddProductActivity).buildService(ProductService::class.java)
        val requestCall : Call<ProductResponse> = productService.createProduct(createProRequest)
        requestCall.enqueue(object: Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if(response.isSuccessful){
                    loadingDialog.dismiss()
                    IonAlert(this@AddProductActivity, IonAlert.SUCCESS_TYPE)
                        .setTitleText("ADDED")
                        .setContentText("Current product added successfully.")
                        .setConfirmClickListener {
                            it.hide()
                            finish()
                        }
                        .show()
                }else{
                    loadingDialog.dismiss()
                    val errorCode:Any= when(response.code()){
                        401 -> {
                            IonAlert(this@AddProductActivity, IonAlert.ERROR_TYPE)
                                .setTitleText("ERROR")
                                .setContentText("401 unauthorized user, please login.")
                                .setConfirmClickListener {
                                    SessionManager(this@AddProductActivity).deleteAccessToken()
                                    it.hide()
                                    finishAffinity()
                                    startActivity(Intent(this@AddProductActivity, LoginActivity::class.java))
                                    Animatoo.animateSplit(this@AddProductActivity)
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
                    IonAlert(this@AddProductActivity, IonAlert.ERROR_TYPE)
                        .setTitleText("ERROR")
                        .setContentText(errorCode.toString())
                        .show()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                loadingDialog.dismiss()
                IonAlert(this@AddProductActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("ERROR")
                    .setContentText("Something went wrong, $t")
                    .show()
            }

        })
    }

}