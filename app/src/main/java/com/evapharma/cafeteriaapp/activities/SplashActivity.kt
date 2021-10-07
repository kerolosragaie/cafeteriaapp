package com.evapharma.cafeteriaapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.evapharma.cafeteriaapp.SPLASH_TIME_OUT
import com.evapharma.cafeteriaapp.USER_DATA
import com.evapharma.cafeteriaapp.api.SessionManager
import com.evapharma.cafeteriaapp.databinding.ActivitySplashBinding
import com.evapharma.cafeteriaapp.models.UserResponse
import java.util.*
import kotlin.concurrent.schedule

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Close night mode:
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //Setting up splash effect:
        splashEffect()
    }

    /**
     * Gives splash effect to logo on start
     * */
    private fun splashEffect() {
            Timer("SettingUp", false).schedule(SPLASH_TIME_OUT) {
            startActivity(Intent(this@SplashActivity, UpdateDeleteProductActivity::class.java))
            //isLoggedInBefore()
        }
    }

    //To check if user logged in before
    private fun isLoggedInBefore(){
        val userResponse : UserResponse? =  SessionManager(this@SplashActivity).fetchAccessToken()
        if(userResponse!= null){
            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
            intent.putExtra(USER_DATA,userResponse)
            startActivity(intent)
            Animatoo.animateShrink(this@SplashActivity)
            finish()
        }else{
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            Animatoo.animateShrink(this@SplashActivity)
            finish()
        }
    }
}