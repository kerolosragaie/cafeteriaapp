package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.USER_DATA
import com.evapharma.cafeteriaapp.api.ApiClient
import com.evapharma.cafeteriaapp.databinding.ActivityLoginBinding
import com.evapharma.cafeteriaapp.helpers.UserHelper
import com.evapharma.cafeteriaapp.models.UserRequest
import com.evapharma.cafeteriaapp.models.UserResponse
import com.evapharma.cafeteriaapp.services.LoginService
import com.evapharma.cafeteriaapp.api.SessionManager
import id.ionbit.ionalert.IonAlert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    //to show or hide loading:
    private lateinit var loadingDialog : IonAlert
    //To get access token:
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = IonAlert(this@LoginActivity, IonAlert.PROGRESS_TYPE)
        .setSpinKit("ThreeBounce")

        sessionManager = SessionManager(this@LoginActivity)

        initUI()


    }

    /**
     * Set up views UI:
     * */
    private fun initUI(){

        //!Login button
        binding.btnLoginLogin.setOnClickListener {
            binding.btnLoginLogin.isActivated = false
            //first validate the fields:
            if(UserHelper.validateData(this@LoginActivity, binding.etLoginEmail, binding.etLoginPassword)){
                loadingDialog.show()
                //second check if email or password are correct:
                val userRequest = UserRequest(binding.etLoginEmail.text.toString(),binding.etLoginPassword.text.toString())
                val loginService:LoginService = ApiClient(this@LoginActivity).buildService(LoginService::class.java)
                val requestCall: Call<UserResponse> = loginService.userLogin(userRequest)
                requestCall.enqueue(object:Callback<UserResponse>{
                    override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                        if(response.isSuccessful){
                            if(response.body()!!.roles[0]=="Admin"){
                                sessionManager.saveAccessToken(response.body()!!)
                                loadingDialog.dismiss()
                                goToHome(response.body()!!)
                            }else{
                                loadingDialog.dismiss()
                                binding.btnLoginLogin.isActivated = true
                                IonAlert(this@LoginActivity, IonAlert.ERROR_TYPE)
                                    .setTitleText("ERROR!")
                                    .setContentText("Only admins can login.")
                                    .show()
                            }
                        }else{
                            val errorCode:String = when(response.code()){
                                400 ->{
                                    "Incorrect email or password."
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
                            binding.btnLoginLogin.isActivated = true
                            IonAlert(this@LoginActivity, IonAlert.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText(errorCode)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        loadingDialog.dismiss()
                        binding.btnLoginLogin.isActivated = true
                        IonAlert(this@LoginActivity, IonAlert.ERROR_TYPE)
                            .setTitleText("ERROR!")
                            .setContentText("$t")
                            .show()
                    }

                })
            }

        }

        //!Reset password button
        binding.tvLoginForgetpass.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SendOTPActivity::class.java))
            Animatoo.animateSlideLeft(this@LoginActivity)
        }
    }

    /**
     * To go to home page with user response data:
     * */
    private fun goToHome(userResponse: UserResponse){
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.putExtra(USER_DATA,userResponse)
        startActivity(intent)
        Animatoo.animateSplit(this@LoginActivity)
        finish()
    }

}