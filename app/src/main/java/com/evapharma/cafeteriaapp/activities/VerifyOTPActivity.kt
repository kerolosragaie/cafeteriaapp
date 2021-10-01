package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.evapharma.cafeteriaapp.databinding.ActivityVerifyOtpactivityBinding
import com.evapharma.cafeteriaapp.PHONE_NUMBER


class VerifyOTPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyOtpactivityBinding
    private var listOfet: List<EditText> = listOf()
    private var sizeofets:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButtons()
        initEditTexts()
        setViewPhonenumber()
    }

    private fun apiGetOTP():String{
        //call api and get the otp code of the user
        return "123456"
    }
    private fun setViewPhonenumber(){
        binding.tvVerifyotpMobilenumber.text=PHONE_NUMBER
    }
    private fun initEditTexts(){
         listOfet=listOf<EditText>(
            binding.etVerifyotpCode1,
            binding.etVerifyotpCode2,
            binding.etVerifyotpCode3,
            binding.etVerifyotpCode4,
            binding.etVerifyotpCode5,
            binding.etVerifyotpCode6)

        sizeofets=listOfet.size

        for (idx in 0 until sizeofets-1){
            listOfet[idx].addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(s.toString().trim().isNotEmpty()){
                        listOfet[idx+1].requestFocus()
                    }
                }
            })
        }
    }

    private fun getInput():String{
        var result=""
        for (idx in 0 until sizeofets){
            result+=listOfet[idx].text.toString()
        }
        return result
    }

    private fun isVerifiedOTP(input:String, otp:String):Boolean{
        return input==otp
    }


    private fun initButtons(){
        binding.btnOnverifyVerifyotp.setOnClickListener {

                if(isVerifiedOTP(getInput(),apiGetOTP() )){
                    intent = Intent(this,NewPasswordActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }
    }
}
