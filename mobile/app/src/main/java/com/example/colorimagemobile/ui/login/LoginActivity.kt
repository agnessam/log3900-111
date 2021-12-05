package com.example.colorimagemobile.ui.login

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.core.widget.doOnTextChanged
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.FormValidator
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.databinding.ActivityLoginBinding
import com.example.colorimagemobile.httpresponsehandler.GlobalHandler
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.ui.home.HomeActivity
import com.example.colorimagemobile.ui.register.FormIndexes
import com.example.colorimagemobile.ui.register.RegisterActivity
import com.example.colorimagemobile.utils.CommonFun.Companion.hideKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.onEnterKeyPressed
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private  var loginActivityViewModel: LoginActivityViewModel= LoginActivityViewModel()
    private lateinit var globalHandler: GlobalHandler
    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var binding: ActivityLoginBinding
    private lateinit var formValidator: FormValidator
    private var canSubmit: Boolean = true

    enum class FormIndexes(val index: Int) {
        EMAIL(0),
        PASSWORD(1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        globalHandler = GlobalHandler()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferencesService = SharedPreferencesService(this)
        val loginLayouts = arrayListOf<TextInputLayout>(binding.emailInputLayout, binding.passwordInputLayout)
        val loginInputs = arrayListOf<TextInputEditText>(binding.emailInputText, binding.passwordInputText)
        formValidator = FormValidator(loginLayouts, loginInputs)
        setListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        binding.loginBtn.setOnClickListener { executeLogin() }
        binding.registerBtn.setOnClickListener { redirectTo(this, RegisterActivity::class.java) }
        binding.loginMain.setOnTouchListener { v, event -> hideKeyboard(this,binding.loginMain) }

        onEnterKeyPressed(binding.emailInputText) { executeLogin() }
        onEnterKeyPressed(binding.passwordInputText) { executeLogin() }

        // inputs error handling
        binding.emailInputText.doOnTextChanged { text, start, before, count ->  handleInputError(text, binding.emailInputLayout) }
        binding.passwordInputText.doOnTextChanged { text, start, before, count ->  handleInputError(text, binding.passwordInputLayout)}
    }

    // handles UI error messages depending on form errors
    private fun handleInputError(text: CharSequence?, inputLayout: TextInputLayout) {
        inputLayout.error = formValidator.getWhitespaceText(text)
        formValidator.validateEmail(FormIndexes.EMAIL.index)
        formValidator.validatePasswordLength(FormIndexes.PASSWORD.index)
        val containsError = formValidator.containsError()
        val invalidInputLength = formValidator.isInputEmpty(resources.getString(R.string.required))

        canSubmit = !containsError && !invalidInputLength
    }

    private fun executeLogin() {
        handleInputError(binding.emailInputText.text, binding.emailInputLayout)
        handleInputError(binding.passwordInputText.text, binding.passwordInputLayout)
        if (!canSubmit) {
            val shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake)
            loginForm.startAnimation(shake);
            return
        }
        val user = UserModel.Login(binding.emailInputText.text.toString(), binding.passwordInputText.text.toString())
        
        // email ok -> make HTTP POST request
        val loginObserver = loginActivityViewModel.loginUser(user)
        loginObserver.observe(this, { handleLoginResponse(it) })
    }

    // response from HTTP request
    private fun handleLoginResponse(HTTPResponse: DataWrapper<HTTPResponseModel.LoginResponse>) {
        printToast(applicationContext, HTTPResponse.message as String)

        // some error occurred during HTTP request
        if (HTTPResponse.isError as Boolean) {
            return
        }
        val response = HTTPResponse.data as HTTPResponseModel.LoginResponse

        // save users info and token and redirect to /Home
        UserService.setToken(response.token)
        UserService.setUserInfo(response.user)
        sharedPreferencesService.setItem(Constants.STORAGE_KEY.TOKEN, response.token)

        redirectTo(this@LoginActivity, HomeActivity::class.java)
    }
}