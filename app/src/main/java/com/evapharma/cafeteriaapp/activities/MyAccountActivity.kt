package com.evapharma.cafeteriaapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.evapharma.cafeteriaapp.databinding.ActivityHomeBinding

class MyAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}