package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.databinding.ActivityAddCategoryBinding
import com.evapharma.cafeteriaapp.databinding.ActivityAddFoodItemBinding

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddCategoryBinding
    private var SELECT_PICTURE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initbtnClickAdd()
        initUploadimg()
        binding.etAddcatCatimgurl.doOnTextChanged { text, start, before, count ->
            Glide.with(this)
                .load(text.toString()) // image url
                .placeholder(R.drawable.ic_meal) // any placeholder to load at start
                .error(R.drawable.ic_error_sign)  // any image in case of error
                .override(139, 130) // resizing
                .centerCrop()
                .into(binding.ivAddcatCatimg)  // imageview object
        }
    }

    fun isValid():Boolean{
        return  binding.etAddcatCatname.text.toString().isNotEmpty() &&
                binding.textinputlayoutAddcatCatimgurl.error==null
    }

    private fun initbtnClickAdd(){
        binding.btnAddcatAdd.setOnClickListener {
        }
    }
    private fun initUploadimg(){
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
                    binding.ivAddcatCatimg.setImageDrawable(null);
                    binding.ivAddcatCatimg.setImageURI(selectedImageUri)
                }
            }
        }
    }
}