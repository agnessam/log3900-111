package com.example.colorimagemobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.colorimagemobile.databinding.ActivityMainBinding
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.ui.home.HomeActivity
import com.example.colorimagemobile.ui.login.LoginActivity
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferencesService: SharedPreferencesService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesService = SharedPreferencesService(this)
        sharedPreferencesService.clear()
        val token = sharedPreferencesService.getItem(Constants.STORAGE_KEY.TOKEN)

        // if token exists, go to Home else go to Login for auth verification
        val destinationActivity: Class<*> = if (token.isNullOrEmpty()) LoginActivity::class.java else HomeActivity::class.java
        redirectTo(this, destinationActivity)
    }
}