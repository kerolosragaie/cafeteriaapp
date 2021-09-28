package com.evapharma.cafeteriaapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.evapharma.cafeteriaapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}