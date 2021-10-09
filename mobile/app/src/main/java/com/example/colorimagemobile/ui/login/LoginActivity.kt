package com.example.colorimagemobile.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.FormValidator
import com.example.colorimagemobile.classes.LoginUser
import com.example.colorimagemobile.databinding.ActivityLoginBinding
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.ui.home.HomeActivity
import com.example.colorimagemobile.ui.register.RegisterActivity
import com.example.colorimagemobile.utils.CommonFun.Companion.closeKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.onEnterKeyPressed
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.CommonFun.Companion.toggleButton
import com.example.colorimagemobile.utils.Constants.Companion.DEBUG_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_TOKEN_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_USERNAME_KEY
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginActivityViewModel
    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var binding: ActivityLoginBinding
    private lateinit var formValidator: FormValidator
    private var canSubmit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesService = SharedPreferencesService(this)
        loginViewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)

        val loginLayouts = arrayListOf<TextInputLayout>(binding.usernameInputLayout, binding.passwordInputLayout)
        val loginInputs = arrayListOf<TextInputEditText>(binding.usernameInputText, binding.passwordInputText)
        formValidator = FormValidator(loginLayouts, loginInputs)

        toggleButton(binding.loginBtn, false) // deactivate login button by default
        setListeners()
    }

    private fun setListeners() {
        binding.loginBtn.setOnClickListener { executeLogin() }
        binding.registerBtn.setOnClickListener { redirectTo(this, RegisterActivity::class.java) }
        binding.loginMain.setOnTouchListener { v, event -> closeKeyboard(this) }

        onEnterKeyPressed(binding.usernameInputText) { executeLogin() }
        onEnterKeyPressed(binding.passwordInputText) { executeLogin() }

        // inputs error handling
        binding.usernameInputText.doOnTextChanged { text, start, before, count ->  handleInputError(text, binding.usernameInputLayout) }
        binding.passwordInputText.doOnTextChanged { text, start, before, count ->  handleInputError(text, binding.passwordInputLayout) }
    }

    // handles UI error messages depending on form errors
    private fun handleInputError(text: CharSequence?, inputLayout: TextInputLayout) {
        val message = if (text!!.contains(" ")) "No spaces are allowed!" else ""
        inputLayout.error = message

        val containsError = formValidator.containsError()
        val invalidInputLength = formValidator.isInputEmpty(resources.getString(R.string.required))

        // activate/deactivate login button if form contains error or one of the inputs is empty
        canSubmit = !containsError && !invalidInputLength
        toggleButton(binding.loginBtn, canSubmit)
    }

    private fun executeLogin() {
        if (!canSubmit) return

        val user = LoginUser(binding.usernameInputText.text.toString(), binding.passwordInputText.text.toString())

        // username ok -> make HTTP POST request
        val loginObserver = loginViewModel.loginUser(user)
        loginObserver.observe(this, { handleLoginResponse(it) })
    }

    // response from HTTP request
    private fun handleLoginResponse(it: DataWrapper<HTTPResponseModel>) {
        printToast(applicationContext, it.message as String)

        // some error occurred during HTTP request
        if (it.isError as Boolean) {
            return
        }

        val userResponse = it.data as HTTPResponseModel
        val token = userResponse.token.toString()
        val username = userResponse.username.toString()

        // save credentials to "local storage"
        sharedPreferencesService.setItem(SHARED_USERNAME_KEY, username)
        sharedPreferencesService.setItem(SHARED_TOKEN_KEY, token)

        // redirect to /chat
        redirectTo(this@LoginActivity, HomeActivity::class.java)
    }
}