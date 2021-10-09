package com.example.colorimagemobile.ui.signUp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.colorimagemobile.ui.login.LoginActivity
import com.example.colorimagemobile.R
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setListeners()
    }

    private fun setListeners() {
        val logInText: TextView = findViewById(R.id.loginText)
        logInText.setOnClickListener { redirectTo(this, LoginActivity::class.java) }
    }
}