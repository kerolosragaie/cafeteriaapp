package com.evapharma.cafeteriaapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.evapharma.cafeteriaapp.databinding.ActivitySplashBinding
import java.util.*
import kotlin.concurrent.schedule

//Constants:
private const val SPLASH_TIME_OUT: Long = 2500

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
            //startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            Animatoo.animateShrink(this@SplashActivity)
            finish()
        }
    }
}