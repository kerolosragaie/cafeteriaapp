package com.evapharma.cafeteriaapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.evapharma.cafeteriaapp.R
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.evapharma.cafeteriaapp.shortToast
import com.google.android.material.textfield.TextInputLayout
import java.net.URL
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.evapharma.cafeteriaapp.CATEGORY_DATA
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.databinding.ActivityAddProductItemBinding
import com.evapharma.cafeteriaapp.models.*
import com.evapharma.cafeteriaapp.services.CategoryService
import com.evapharma.cafeteriaapp.services.ProductService
import id.ionbit.ionalert.IonAlert
import retrofit2.*


class AddProductActivity : AppCompatActivity() {
    var mealsEditTextList = mutableListOf<EditText>()
    var mealsTextLayoutList = mutableListOf<TextInputLayout>()
    private lateinit var binding:ActivityAddProductItemBinding
    private val MIN_LEN =3
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
            .setSpinKit("ThreeBounce")

        initEtsList()
        initTilsList()
        initTextLayoutsEts()
        initbtnClickAdd()
        initUploadimg()
        loadCurrentCatData()
    }

    private fun initUploadimg(){
        binding.imgAddfooditemUpmealimg.setOnClickListener {
            imageChooser()
        }
        binding.btnAddfooditemAdd.setOnClickListener {
            callAddProductApi()
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
                    binding.ivAddfooditemMealimg.setImageDrawable(null);
                    binding.ivAddfooditemMealimg.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun initEtsList(){
        mealsEditTextList = mutableListOf<EditText>(
            binding.etAddfooditemMealname,
            binding.etAddfooditemMealprice,
            binding.etAddfooditemDescription,
        )
    }

    //text input layout initialization
    private fun initTilsList(){
        mealsTextLayoutList = mutableListOf<TextInputLayout>(
            binding.textinputlayoutAddfooditemMealname,
            binding.tiAddfooditemMealprice,
            binding.tiAddfooditemMealdescription
        )
    }

    private fun initTextLayoutsEts() {
        for (idx in 0 until mealsEditTextList.size)
            mealsEditTextList[idx].doOnTextChanged { text, start, before, count ->
                if (text.toString().trim().length < MIN_LEN) {
                    mealsTextLayoutList[idx].error =
                        "Minimum Characters for this field is $MIN_LEN"
                } else {
                    mealsTextLayoutList[idx].error = null
                }
            }

        binding.etAddfooditemMealimgurl.doOnTextChanged { text, start, before, count ->
            Glide.with(this)
                .load(text.toString()) // image url
                .placeholder(R.drawable.ic_meal) // any placeholder to load at start
                .error(R.drawable.ic_error_sign)  // any image in case of error
                .override(139, 130) // resizing
                .centerCrop()
                .into(binding.ivAddfooditemMealimg)  // imageview object
        }

    }

    private fun urlHasImage(url:String):Boolean{
        val connection = URL(url).openConnection()
        val contentType = connection.getHeaderField("Content-Type")
        val image:Boolean = contentType.startsWith("image/")
        return image
    }

    private fun initbtnClickAdd(){
        binding.btnAddfooditemAdd.setOnClickListener {
            println(isValidMeal())
        }
    }

    private fun isValidMeal():Boolean{
        mealsEditTextList.forEach{
            if(it.text.toString().trim().length<MIN_LEN)
            {
                shortToast(this,"Is NOT VALID YA KALB")
                return false
            }
        }
        shortToast(this,"Is valid ya negm")
        return true
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
        createProRequest.imageUrl=binding.etAddfooditemMealimgurl.text.toString()
        createProRequest.price = binding.etAddfooditemMealprice.text.toString().toDouble()
        createProRequest.name = binding.etAddfooditemMealname.text.toString()
        createProRequest.inOffers = binding.switcherAddFoodOffers.isChecked
        createProRequest.categoryId = currentCatResponse.id

        val productService:ProductService = ApiClient(this@AddProductActivity).buildService(ProductService::class.java)
        val requestCall : Call<ProductResponse> = productService.createProduct(createProRequest)
        requestCall.enqueue(object: Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if(response.isSuccessful){
                    loadingDialog.dismiss()
                    IonAlert(this@AddProductActivity, IonAlert.SUCCESS_TYPE)
                        .setTitleText("ADDED")
                        .setContentText("Current category added successfully")
                        .setConfirmClickListener {
                            finish()
                        }
                        .show()
                }else{
                    loadingDialog.dismiss()
                    val errorCode:String = when(response.code()){
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
                        .setContentText("Something went wrong, ${response.errorBody()}")
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