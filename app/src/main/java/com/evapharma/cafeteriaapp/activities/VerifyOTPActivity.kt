package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.evapharma.cafeteriaapp.databinding.ActivityVerifyOtpactivityBinding
import com.evapharma.cafeteriaapp.PHONE_NUMBER
import com.evapharma.cafeteriaapp.PHONE_RESPONSE
import com.evapharma.cafeteriaapp.USER_DATA
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.models.SendOtpRequest
import com.evapharma.cafeteriaapp.models.SendOtpResponse
import com.evapharma.cafeteriaapp.services.ResetPasswordService
import id.ionbit.ionalert.IonAlert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VerifyOTPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyOtpactivityBinding
    private var listOfet: List<EditText> = listOf()
    private var sizeofets:Int=0

    //to show or hide loading:
    private lateinit var loadingDialog : IonAlert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButtons()
        initEditTexts()
        setViewPhonenumber()
        setUpTimer()
    }

    //when back pressed animate backward:
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this@VerifyOTPActivity)
    }
    //Set up timer on page start:
    private fun setUpTimer(){
        object :CountDownTimer(300000,1000){
            override fun onTick(p0: Long) {
                val minute = (p0 / 1000) / 60
                val seconds = (p0 / 1000) % 60
                binding.tvVerifyotpTimer.text = "$minute : $seconds"
            }

            override fun onFinish() {
                IonAlert(this@VerifyOTPActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("TIMEOUT")
                    .setContentText("OTP expired, please request new one.")
                    .setConfirmText("Ok")
                    .setConfirmClickListener {
                        finish()
                        Animatoo.animateSlideRight(this@VerifyOTPActivity)
                    }.show()
            }
        }.start()
    }

    //to phone response from last page use if needed:
    private fun getPhoneResponse():String?{
        val bundle:Bundle? = intent.extras
        if(bundle?.containsKey(PHONE_RESPONSE)!!) {
            return intent.extras?.get(USER_DATA) as String
        }
        return null
    }

    private fun apiGetOTP(){
        if(isEtValid()){
            //call api and get the otp code of the user
            callApi()
        }
    }

    //check if all ETs are valid:
    private fun isEtValid():Boolean{
        var result:String=""
        for (idx in 0 until sizeofets-1){
            result+=listOfet[idx].text
        }
        return result.trim().length==sizeofets
    }

    //call api:
    private fun callApi(){
        loadingDialog.show()
        binding.btnOnverifyVerifyotp.isActivated=false
        val resetPasswordService:ResetPasswordService = ApiClient(this@VerifyOTPActivity).buildService(ResetPasswordService::class.java)
        val requestCall: Call<SendOtpResponse> = resetPasswordService.sendOtp(SendOtpRequest(getInput(),binding.tvResendotpVerifyotp.text.toString()))
        requestCall.enqueue(object: Callback<SendOtpResponse>{
            override fun onResponse(call: Call<SendOtpResponse>, response: Response<SendOtpResponse>) {
                if(response.isSuccessful){
                    loadingDialog.dismiss()
                    binding.btnOnverifyVerifyotp.isActivated=true
                    if(response.body()!!.isCorrect){
                        val intent = Intent(this@VerifyOTPActivity,NewPasswordActivity::class.java)
                        intent.putExtra(PHONE_RESPONSE,getPhoneResponse())
                        startActivity(Intent(this@VerifyOTPActivity,NewPasswordActivity::class.java))
                        Animatoo.animateSlideLeft(this@VerifyOTPActivity)
                        finish()
                    }else{
                        IonAlert(this@VerifyOTPActivity, IonAlert.ERROR_TYPE)
                            .setTitleText("ERROR!")
                            .setContentText("Wrong OTP, try again.")
                            .show()
                    }
                }else{
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
                    loadingDialog.dismiss()
                    binding.btnOnverifyVerifyotp.isActivated=true
                    IonAlert(this@VerifyOTPActivity, IonAlert.ERROR_TYPE)
                        .setTitleText("ERROR!")
                        .setContentText(response.code().toString())
                        .show()
                }
            }

            override fun onFailure(call: Call<SendOtpResponse>, t: Throwable) {
                loadingDialog.dismiss()
                binding.btnOnverifyVerifyotp.isActivated=true
                IonAlert(this@VerifyOTPActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("ERROR!")
                    .setContentText("$t")
                    .show()
            }

        })
    }

    private fun setViewPhonenumber(){
        binding.tvVerifyotpMobilenumber.text=PHONE_NUMBER
    }
    private fun initEditTexts(){
         listOfet=listOf(
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
            apiGetOTP()
        }

        binding.tvResendotpVerifyotp.setOnClickListener {
            startActivity(Intent(this,SendOTPActivity::class.java))
            Animatoo.animateSlideRight(this@VerifyOTPActivity)
            finish()
        }

    }
}
