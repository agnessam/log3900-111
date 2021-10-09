package com.example.colorimagemobile.ui.register

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.example.colorimagemobile.databinding.ActivityRegisterBinding
import com.example.colorimagemobile.ui.login.LoginActivity
import com.example.colorimagemobile.utils.CommonFun.Companion.closeKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.CommonFun.Companion.toggleButton
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggleButton(binding.registerBtn, false) // deactivate login button by default
        setListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        binding.loginText.setOnClickListener { redirectTo(this, LoginActivity::class.java) }
        binding.registerMain.setOnTouchListener { v, event -> closeKeyboard(this) }

        binding.usernameInputText.doOnTextChanged { text, start, before, count ->  handleInputError(text, binding.usernameInputLayout) }
    }

    private fun handleInputError(text: CharSequence?, inputLayout: TextInputLayout) {
        val message = if (text!!.contains(" ")) "No spaces are allowed!" else ""
        inputLayout.error = message

    }
}