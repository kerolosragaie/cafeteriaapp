package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.evapharma.cafeteriaapp.*
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.databinding.ActivitySendOtpactivityBinding
import com.evapharma.cafeteriaapp.functions.shortToast
import com.evapharma.cafeteriaapp.models.PhoneResponse
import com.evapharma.cafeteriaapp.models.SendPhoneRequest
import com.evapharma.cafeteriaapp.services.ResetPasswordService
import id.ionbit.ionalert.IonAlert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendOTPActivity : AppCompatActivity() {
    lateinit var binding: ActivitySendOtpactivityBinding
    //to show or hide loading:
    private lateinit var loadingDialog : IonAlert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendOtpactivityBinding.inflate(layoutInflater)

        loadingDialog = IonAlert(this@SendOTPActivity, IonAlert.PROGRESS_TYPE)
            .setSpinColor("#053776").setSpinKit("ThreeBounce")

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

    private fun getOtp(phone:String):Boolean{
       if ( !isFormatPhone(phone)){
           shortToast(this,"Wrong phone format.")
           return false
       }
        if ( !isCorrectLength(phone, PHONE_LENGTH)){
            shortToast(this,"Wrong Phone number length.")
            return false
        }

        return true
    }

    private fun initEditTexts(){
        binding.etSendotpInputmobile.doOnTextChanged { text, start, before, count ->
            PHONE_NUMBER= text.toString()
        }
    }

    //when back pressed animate backward:
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this@SendOTPActivity)
    }

    //init buttons and call API
    private fun initButtons(){
        binding.btnGetotpSendotp.setOnClickListener {
            if(binding.etSendotpInputmobile.text.isNotEmpty() && binding.etSendotpInputmobile.text.length==PHONE_LENGTH){
                callAPI(binding.etSendotpInputmobile.text.toString())
            }else{
                IonAlert(this@SendOTPActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("ERROR")
                    .setContentText("Wrong phone format.")
                    .show()
            }
        }
    }

    private fun callAPI(phone:String){
        //call api here:
        loadingDialog.show()
        binding.btnGetotpSendotp.isActivated=false
        val resetPasswordService:ResetPasswordService = ApiClient(this@SendOTPActivity).buildService(ResetPasswordService::class.java)
        val requestCall : Call<PhoneResponse> = resetPasswordService.sendPhoneNumber(SendPhoneRequest(phone))
        requestCall.enqueue(object: Callback<PhoneResponse>{
            override fun onResponse(call: Call<PhoneResponse>, response: Response<PhoneResponse>) {
                if(response.isSuccessful){
                    loadingDialog.dismiss()
                    binding.btnGetotpSendotp.isActivated=true
                    val intent = Intent(this@SendOTPActivity, VerifyOTPActivity::class.java)
                    intent.putExtra(PHONE_RESPONSE,binding.etSendotpInputmobile.text.toString())
                    startActivity(intent)
                    Animatoo.animateSlideLeft(this@SendOTPActivity)
                    finish()
                }else{
                    val errorCode:String = when(response.code()){
                        400 -> {
                            "Phone number is not existed."
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
                    loadingDialog.dismiss()
                    binding.btnGetotpSendotp.isActivated=true
                    IonAlert(this@SendOTPActivity, IonAlert.ERROR_TYPE)
                        .setTitleText("ERROR")
                        .setContentText(errorCode)
                        .show()
                }
            }

            override fun onFailure(call: Call<PhoneResponse>, t: Throwable) {
                loadingDialog.dismiss()
                binding.btnGetotpSendotp.isActivated=true
                IonAlert(this@SendOTPActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("ERROR")
                    .setContentText("$t")
                    .show()
            }

        })
    }

}