package com.example.colorimagemobile.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.colorimagemobile.R
import com.example.colorimagemobile.databinding.ActivityLoginBinding
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.classes.User
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.ui.home.HomeActivity
import com.example.colorimagemobile.ui.signUp.SignUpActivity
import com.example.colorimagemobile.utils.CommonFun.Companion.closeKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.onEnterKeyPressed
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.CommonFun.Companion.toggleButton
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_TOKEN_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_USERNAME_KEY
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginActivityViewModel
    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var binding: ActivityLoginBinding
    private var canSubmit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferencesService = SharedPreferencesService(this)
        loginViewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)

        toggleButton(binding.loginBtn, false) // deactivate login button by default
        setListeners()
    }

    private fun setListeners() {
        binding.loginBtn.setOnClickListener { executeLogin() }

        val registerBtn: Button = findViewById(R.id.registerBtn)
        registerBtn.setOnClickListener { redirectTo(this, SignUpActivity::class.java) }

        onEnterKeyPressed(binding.usernameInputText) { executeLogin() }
        onEnterKeyPressed(binding.passwordInputText) { executeLogin() }

        val loginMain: ConstraintLayout = findViewById(R.id.loginMain)
        loginMain.setOnTouchListener { v, event -> closeKeyboard(this) }

        // inputs error handling
        binding.usernameInputText.doOnTextChanged { text, start, before, count ->  handleInputError(text, binding.usernameInputLayout) }
        binding.passwordInputText.doOnTextChanged { text, start, before, count ->  handleInputError(text, binding.passwordInputLayout) }
    }

    // handles UI error messages depending on form errors
    private fun handleInputError(text: CharSequence?, inputLayout: TextInputLayout) {
        val message = if (text!!.contains(" ")) "No spaces are allowed!" else ""
        inputLayout.error = message

        // contains some kind of error
        val containsError = binding.usernameInputLayout.error.isNullOrEmpty() && binding.passwordInputLayout.error.isNullOrEmpty()

        // check if inputs is empty
        val isUsernameInvalid = binding.usernameInputText.text.isNullOrEmpty()
        val isPasswordInvalid = binding.passwordInputText.text.isNullOrEmpty()
        val invalidInputLength = isUsernameInvalid || isPasswordInvalid

        // show or hide required keyword below inputs
        val requiredString = resources.getString(R.string.required);
        binding.usernameInputLayout.helperText = if (isUsernameInvalid) requiredString else ""
        binding.passwordInputLayout.helperText = if (isPasswordInvalid) requiredString else ""

        // activate/deactivate login button if form contains error or isEmpty
        canSubmit = containsError && !invalidInputLength
        toggleButton(binding.loginBtn, canSubmit)
    }

    private fun executeLogin() {
        if (!canSubmit) return

        val user = User(binding.usernameInputText.text.toString(), binding.passwordInputText.text.toString())

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