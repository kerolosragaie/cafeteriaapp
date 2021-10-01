package com.evapharma.cafeteriaapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.evapharma.cafeteriaapp.MIN_PASSWORD_LENGTH
import com.evapharma.cafeteriaapp.databinding.ActivityNewPasswordBinding
import com.evapharma.cafeteriaapp.shortToast


class NewPasswordActivity : AppCompatActivity() {
    
    lateinit var binding:ActivityNewPasswordBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding= ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEditTexts()
        initButtons()
    }
    
    private fun initEditTexts(){
        binding.etNewpasswordEnterpass.doOnTextChanged { text, start, before, count ->
            if(text!!.length< MIN_PASSWORD_LENGTH){
                binding.textInputlayoutPass.error=
                    "Minimum length for password is $MIN_PASSWORD_LENGTH"
            }else{
                binding.textInputlayoutPass.error=null
            }
        }

        binding.etNewpasswordConfirmpass.doOnTextChanged { text, start, before, count ->
            if(text.toString() != binding.etNewpasswordEnterpass.text.toString()){
                binding.textInputlayoutConfirmpass.error="Password doesn't match the field above"
            }else{
                binding.textInputlayoutConfirmpass.error=null
            }
        }
    }

    private fun initButtons(){
        binding.btnNewpasswordUpdate.setOnClickListener {
            updatePassword()
        }
    }

    private fun isNotValid(str1:String, str2:String, len:Int):Boolean{

        return (str1.length < len) || (str1!=str2)

    }

    private fun updatePassword(){

      if(  isNotValid(binding.etNewpasswordEnterpass.text.toString(),
                   binding.etNewpasswordConfirmpass.text.toString(), MIN_PASSWORD_LENGTH ) )
        return //breaks the function

        shortToast(this,"login")
        //api call goes here to save password

        //redirect to login page using intent :)

        finish()

    }

}