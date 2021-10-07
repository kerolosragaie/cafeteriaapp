package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.evapharma.cafeteriaapp.*
import com.evapharma.cafeteriaapp.databinding.ActivityAddProductItemBinding
import com.evapharma.cafeteriaapp.databinding.ActivityUpdateDeleteProductBinding

class UpdateDeleteProductActivity : AppCompatActivity() {

    private lateinit var binding:ActivityUpdateDeleteProductBinding
    private  var SELECT_PICTURE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateDeleteProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initEts()
        initBtns()
        initUploadimg()
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
                //TODO: call api to delete product
            }
        }
        binding.btnUdproductDelete.setOnClickListener {
            validateForm()
            if(isValid()){
                //TODO:call api to update product

            }
        }
    }

}