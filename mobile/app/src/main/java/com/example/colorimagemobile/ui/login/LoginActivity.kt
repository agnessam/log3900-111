package com.example.colorimagemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.User
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.ui.home.HomeActivity
import com.example.colorimagemobile.ui.login.LoginActivityViewModel
import com.example.colorimagemobile.ui.signUp.SignUpActivity
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
    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var usernameInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var loginBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)
        sharedPreferencesService = SharedPreferencesService(this)
        usernameEditText = findViewById(R.id.usernameInputText)
        passwordEditText = findViewById(R.id.passwordInputText)
        loginBtn = findViewById(R.id.loginBtn)
        usernameInputLayout = findViewById(R.id.usernameInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)

        toggleButton(loginBtn, false) // deactivate login button by default
        setListeners()
    }

    private fun setListeners() {
        loginBtn.setOnClickListener { executeLogin() }

        val registerBtn: Button = findViewById(R.id.registerBtn)
        registerBtn.setOnClickListener { redirectTo(this, SignUpActivity::class.java) }

        onEnterKeyPressed(usernameEditText) { executeLogin() }
        onEnterKeyPressed(passwordEditText) { executeLogin() }

        val loginMain: ConstraintLayout = findViewById(R.id.loginMain)
        loginMain.setOnTouchListener { v, event -> closeKeyboard(this) }

        // inputs error handling
        usernameEditText.doOnTextChanged { text, start, before, count ->  handleInputError(text, usernameInputLayout) }
        passwordEditText.doOnTextChanged { text, start, before, count ->  handleInputError(text, passwordInputLayout) }
    }

    // handles UI error messages depending on form errors
    private fun handleInputError(text: CharSequence?, inputLayout: TextInputLayout) {
        val message = if (text!!.contains(" ")) "No spaces are allowed!" else ""
        inputLayout.error = message

        // contains some kind of error
        val containsError = usernameInputLayout.error.isNullOrEmpty() && passwordInputLayout.error.isNullOrEmpty()

        // check if inputs is empty
        val isUsernameInvalid = usernameEditText.text.isNullOrEmpty()
        val isPasswordInvalid = passwordEditText.text.isNullOrEmpty()
        val invalidInputLength = isUsernameInvalid || isPasswordInvalid

        // show or hide required keyword below inputs
        val requiredString = resources.getString(R.string.required);
        usernameInputLayout.helperText = if (isUsernameInvalid) requiredString else ""
        passwordInputLayout.helperText = if (isPasswordInvalid) requiredString else ""

        // deactivate login button if form contains error or isEmpty
        toggleButton(loginBtn, containsError && !invalidInputLength)
    }

    private fun executeLogin() {
        val user = User(usernameEditText.text.toString(), passwordEditText.text.toString())

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