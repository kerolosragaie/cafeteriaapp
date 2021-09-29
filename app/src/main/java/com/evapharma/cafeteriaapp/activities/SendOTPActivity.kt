package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.evapharma.cafeteriaapp.databinding.ActivitySendOtpactivityBinding
import com.evapharma.cafeteriaapp.shortToast
import com.evapharma.cafeteriaapp.longToast
import com.evapharma.cafeteriaapp.PHONE_NUMBER

const val STARTER:String="0"
const val PHONE_LENGTH:Int=11

class SendOTPActivity : AppCompatActivity() {

    lateinit var binding: ActivitySendOtpactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButtons()
        initEditTexts()

    }

    private fun isFormatPhone(phone:String):Boolean{
        return phone.startsWith(STARTER)
    }

    private fun isCorrectLength(phone:String,target_length:Int):Boolean{
        return phone.length==target_length
    }

    private fun isValidPhone(phone:String):Boolean{
        //api call to verify yes or no

        return true // change later
    }

    private fun getOtp(phone:String):Boolean{

       if ( !isFormatPhone(phone)){
           shortToast(this,"Wrong phone format!")
           return false
       }
        if ( !isCorrectLength(phone, PHONE_LENGTH)){
            shortToast(this,"Wrong Phone number length!")
            return false
        }

        if ( !isValidPhone(phone)){
            longToast(this,"Enter the correct phone number assigned to your account!")
            return false
        }

        return true
    }

    fun initButtons(){
        binding.btnGetotpSendotp.setOnClickListener {
            if(getOtp(PHONE_NUMBER)){
                val intent = Intent(this, VerifyOTPActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    private fun initEditTexts(){
        binding.etSendotpInputmobile.doOnTextChanged { text, start, before, count ->
            PHONE_NUMBER= text.toString()
        }
    }
}