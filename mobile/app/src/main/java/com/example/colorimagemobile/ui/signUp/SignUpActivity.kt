package com.example.colorimagemobile.ui.signUp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.colorimagemobile.LoginActivity
import com.example.colorimagemobile.R
import com.example.colorimagemobile.utils.CommonFun
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