package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.evapharma.cafeteriaapp.databinding.ActivityLoginBinding
import com.evapharma.cafeteriaapp.helpers.UserHelper
import com.evapharma.cafeteriaapp.models.User
import id.ionbit.ionalert.IonAlert

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    /**
     * Set up views UI:
     * */
    private fun initUI(){
        binding.btnLoginLogin.setOnClickListener {
            //first validate the fields:
            if(UserHelper.validateData(this@LoginActivity, binding.etLoginEmail, binding.etLoginPassword,)){
                //second check if email or password are correct:
                if(binding.etLoginEmail.text.toString()== User.email
                    && binding.etLoginPassword.text.toString()== User.password){
                    //TODO: add login page here, using retrofit:
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    Animatoo.animateSplit(this@LoginActivity)
                    finish()
                }else{
                    //Wrong email or password:
                    IonAlert(this, IonAlert.ERROR_TYPE)
                        .setTitleText("ERROR!")
                        .setContentText("Wrong email or password!")
                        .show()
                }
            }
        }
    }
}