package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.evapharma.cafeteriaapp.USER_DATA
import com.evapharma.cafeteriaapp.databinding.ActivityLoginBinding
import com.evapharma.cafeteriaapp.helpers.UserHelper
import com.evapharma.cafeteriaapp.models.User
import com.evapharma.cafeteriaapp.models.UserRequest
import com.evapharma.cafeteriaapp.models.UserResponse
import com.evapharma.cafeteriaapp.services.ApiClient
import com.evapharma.cafeteriaapp.services.LoginService
import com.evapharma.cafeteriaapp.services.SessionManager
import id.ionbit.ionalert.IonAlert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    //to show or hide loading:
    private lateinit var loadingDialog : IonAlert
    //To get access token:
    private lateinit var sessionManager:SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = IonAlert(this@LoginActivity, IonAlert.PROGRESS_TYPE)
        .setSpinKit("ThreeBounce")

        sessionManager = SessionManager(this@LoginActivity)

        initUI()


    }

    override fun onResume() {
        super.onResume()
        //check if user was logged in
        isLoggedInBefore()
    }

    /**
     * Set up views UI:
     * */
    private fun initUI(){
        binding.btnLoginLogin.setOnClickListener {
            //first validate the fields:
            if(UserHelper.validateData(this@LoginActivity, binding.etLoginEmail, binding.etLoginPassword,)){
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
                            IonAlert(this@LoginActivity, IonAlert.ERROR_TYPE)
                                .setTitleText("ERROR!")
                                .setContentText("$errorCode")
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        loadingDialog.dismiss()
                        IonAlert(this@LoginActivity, IonAlert.ERROR_TYPE)
                            .setTitleText("ERROR!")
                            .setContentText("$t")
                            .show()
                    }

                })
            }

        }
    }

    //To check if user logged in before
    private fun isLoggedInBefore(){
        loadingDialog.show()
        val userResponse :UserResponse? =  sessionManager.fetchAccessToken()
        if(userResponse!= null){
            goToHome(userResponse)
        }
        loadingDialog.dismiss()
    }

    private fun goToHome(userResponse: UserResponse){
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.putExtra(USER_DATA,userResponse)
        startActivity(intent)
        Animatoo.animateSplit(this@LoginActivity)
        finish()
    }

}