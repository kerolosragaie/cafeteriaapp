package com.evapharma.cafeteriaapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.evapharma.cafeteriaapp.MIN_PASSWORD_LENGTH
import com.evapharma.cafeteriaapp.PHONE_RESPONSE
import com.evapharma.cafeteriaapp.USER_DATA
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.databinding.ActivityNewPasswordBinding
import com.evapharma.cafeteriaapp.models.ResetPasswordRequest
import com.evapharma.cafeteriaapp.models.ResetPasswordResponse
import com.evapharma.cafeteriaapp.services.ResetPasswordService
import id.ionbit.ionalert.IonAlert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewPasswordActivity : AppCompatActivity() {
    
    private lateinit var binding:ActivityNewPasswordBinding
    //to show or hide loading:
    private lateinit var loadingDialog : IonAlert
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = IonAlert(this@NewPasswordActivity, IonAlert.PROGRESS_TYPE)
            .setSpinColor("#053776").setSpinKit("ThreeBounce")

        initEditTexts()
        initButtons()
    }

    //when back pressed animate backward:
    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideRight(this@NewPasswordActivity)
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

      if(isNotValid(binding.etNewpasswordEnterpass.text.toString(),
                   binding.etNewpasswordConfirmpass.text.toString(), MIN_PASSWORD_LENGTH ) )
        return //breaks the function

       callApi()


    }

    //to phone response from last page use if needed:
    private fun getPhoneResponse():String?{
        val bundle:Bundle? = intent.extras
        if(bundle?.containsKey(PHONE_RESPONSE)!!) {
            return intent.extras?.get(USER_DATA) as String
        }
        return null
    }

    //call api
    private fun callApi(){

        loadingDialog.show()
        binding.btnNewpasswordUpdate.isActivated=false

        //api call goes here to save password
        val phoneNumber = getPhoneResponse()
        val resetPasswordRequest = ResetPasswordRequest(
            binding.etNewpasswordEnterpass.text.toString(),
            phoneNumber!!
        )
        val resetPasswordService:ResetPasswordService = ApiClient(this@NewPasswordActivity).buildService(ResetPasswordService::class.java)
        val requestCall : Call<ResetPasswordResponse> = resetPasswordService.resetPassword(resetPasswordRequest)
        requestCall.enqueue(object :Callback<ResetPasswordResponse>{
            override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {
                if(response.isSuccessful){
                    loadingDialog.dismiss()
                    binding.btnNewpasswordUpdate.isActivated=true
                    if(response.body()!!.isChanged){
                        IonAlert(this@NewPasswordActivity, IonAlert.SUCCESS_TYPE)
                            .setTitleText("PASSWORD CHANGED")
                            .setContentText("Password changed successfully, please login.")
                            .setConfirmClickListener {
                                finish()
                                Animatoo.animateSlideLeft(this@NewPasswordActivity)
                            }
                            .show()
                    }else{
                        IonAlert(this@NewPasswordActivity, IonAlert.ERROR_TYPE)
                            .setTitleText("ERROR!")
                            .setContentText("Wrong password format.")
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
                    binding.btnNewpasswordUpdate.isActivated=true
                    IonAlert(this@NewPasswordActivity, IonAlert.ERROR_TYPE)
                        .setTitleText("ERROR!")
                        .setContentText(response.code().toString())
                        .show()
                }

            }

            override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                loadingDialog.dismiss()
                binding.btnNewpasswordUpdate.isActivated=true
                IonAlert(this@NewPasswordActivity, IonAlert.ERROR_TYPE)
                    .setTitleText("ERROR!")
                    .setContentText("$t")
                    .show()
            }

        })
    }

}