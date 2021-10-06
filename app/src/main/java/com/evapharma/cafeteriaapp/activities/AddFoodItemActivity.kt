package com.evapharma.cafeteriaapp.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import com.evapharma.cafeteriaapp.R
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.evapharma.cafeteriaapp.databinding.ActivityAddFoodItemBinding
import com.evapharma.cafeteriaapp.shortToast
import com.google.android.material.textfield.TextInputLayout
import java.net.URL
import android.content.Intent
import android.net.Uri

import androidx.core.app.ActivityCompat.startActivityForResult


class AddFoodItemActivity : AppCompatActivity() {

    var mealsEditTextList = mutableListOf<EditText>()
    var mealsTextLayoutList = mutableListOf<TextInputLayout>()
    private lateinit var binding:ActivityAddFoodItemBinding
    private val MIN_LEN =3
    private var SELECT_PICTURE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddFoodItemBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initEtsList()
        initTilsList()
        initTextLayoutsEts()
        initbtnClickAdd()
        initUploadimg()

    }

    private fun initUploadimg(){
        binding.imgAddfooditemUpmealimg.setOnClickListener {
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

    //text input layout initalization
    private fun initTilsList(){
        mealsTextLayoutList = mutableListOf<TextInputLayout>(
            binding.textinputlayoutAddfooditemMealname,
            binding.textinputlayoutAddfooditemMealprice,
            binding.textinputlayoutAddfooditemMealdescription
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


}