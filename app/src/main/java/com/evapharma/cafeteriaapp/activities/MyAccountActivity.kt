package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.evapharma.cafeteriaapp.R
import com.evapharma.cafeteriaapp.USER_DATA
import com.evapharma.cafeteriaapp.api.SessionManager
import com.evapharma.cafeteriaapp.databinding.ActivityMyAccountBinding
import com.evapharma.cafeteriaapp.models.UserResponse
import id.ionbit.ionalert.IonAlert

class MyAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyAccountBinding
    //current user data:
    lateinit var currentUserResponse :UserResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get user account data:
        loadUserData()

        initButtons()

    }

    private fun initButtons(){
        binding.btnMyaccountLogout.setOnClickListener {
            SessionManager(this@MyAccountActivity).deleteAccessToken()
            startActivity(Intent(this@MyAccountActivity, LoginActivity::class.java))
            Animatoo.animateCard(this@MyAccountActivity)
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Animatoo.animateSlideUp(this@MyAccountActivity)
    }


    //to get user data and show in the drawer:
    private fun loadUserData(){
        val bundle:Bundle? = intent.extras
        if(bundle?.containsKey(USER_DATA)!=null){
            currentUserResponse = intent.extras?.get(USER_DATA) as UserResponse
        }
        binding.tvMyaccountUsername.text = currentUserResponse.username
        binding.tvMyaccountEmail.text = currentUserResponse.email
    }

}