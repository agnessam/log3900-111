package com.example.colorimagemobile.ui.register

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.FormValidator
import com.example.colorimagemobile.databinding.ActivityRegisterBinding
import com.example.colorimagemobile.ui.login.LoginActivity
import com.example.colorimagemobile.utils.CommonFun.Companion.closeKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.CommonFun.Companion.toggleButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var formValidator: FormValidator
    private var canSubmit: Boolean = false
    private final val EMAIL_INDEX = 3
    private lateinit var registerLayouts: ArrayList<TextInputLayout>
    private lateinit var registerInputs: ArrayList<TextInputEditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerLayouts = arrayListOf<TextInputLayout>(binding.firstNameInputLayout, binding.lastNameInputLayout, binding.usernameInputLayout, binding.emailInputLayout, binding.passwordInputLayout, binding.confirmPasswordInputLayout)
        registerInputs = arrayListOf<TextInputEditText>(binding.firstNameInputText, binding.lastNameInputText, binding.usernameInputText, binding.emailInputText, binding.passwordInputText, binding.confirmPasswordInputText)
        formValidator = FormValidator(registerLayouts, registerInputs)

        toggleButton(binding.registerBtn, false) // deactivate login button by default
        setListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        binding.loginText.setOnClickListener { redirectTo(this, LoginActivity::class.java) }
        binding.registerMain.setOnTouchListener { v, event -> closeKeyboard(this) }

        // add text listener to each input
        registerInputs.forEachIndexed { index, input ->
            input.doOnTextChanged { text, start, before, count ->
                handleInputError(text, registerLayouts[index])
            }
        }
    }

    private fun handleInputError(text: CharSequence?, inputLayout: TextInputLayout) {
        inputLayout.error = formValidator.getWhitespaceText(text)

        formValidator.validateEmail(EMAIL_INDEX)
        val containsError = formValidator.containsError()
        val invalidInputLength = formValidator.isInputEmpty(resources.getString(R.string.required))

        // activate/deactivate login button if form contains error or one of the inputs is empty
        canSubmit = !containsError && !invalidInputLength
        toggleButton(binding.registerBtn, canSubmit)
    }
}