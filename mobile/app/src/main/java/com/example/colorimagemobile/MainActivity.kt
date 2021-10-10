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
        val token = sharedPreferencesService.getItem(Constants.STORAGE_KEY.TOKEN)

        if (token.isNullOrEmpty()) {
            // redirect to /login
            redirectTo(this, LoginActivity::class.java)
        } else {
            // redirect to /home
            redirectTo(this, HomeActivity::class.java)
        }
    }
}