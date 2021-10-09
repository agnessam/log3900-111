package com.example.colorimagemobile.ui.register

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.FormValidator
import com.example.colorimagemobile.classes.RegisterNewUser
import com.example.colorimagemobile.databinding.ActivityRegisterBinding
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.ui.login.LoginActivity
import com.example.colorimagemobile.utils.CommonFun.Companion.closeKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.CommonFun.Companion.toggleButton
import com.example.colorimagemobile.utils.Constants.Companion.DEBUG_KEY
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

enum class FORM_INDEXES(val index: Int) {
    FIRST_NAME(0),
    LAST_NAME(1),
    USERNAME(2),
    EMAIL(3),
    PASSWORD(4),
    PASSWORD_CONFIRMATION(5)
}

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerViewModel: RegisterActivityViewModel
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var formValidator: FormValidator
    private var canSubmit: Boolean = false
    private lateinit var registerLayouts: ArrayList<TextInputLayout>
    private lateinit var registerInputs: ArrayList<TextInputEditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = ViewModelProvider(this).get(RegisterActivityViewModel::class.java)

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
        binding.registerBtn.setOnClickListener { executeRegister() }

        // add text listener to each input
        registerInputs.forEachIndexed { index, input ->
            input.doOnTextChanged { text, start, before, count ->
                handleInputError(text, registerLayouts[index])
            }
        }
    }

    private fun handleInputError(text: CharSequence?, inputLayout: TextInputLayout) {
        inputLayout.error = formValidator.getWhitespaceText(text)

        formValidator.validateEmail(FORM_INDEXES.EMAIL.index)
        val containsError = formValidator.containsError()
        val invalidInputLength = formValidator.isInputEmpty(resources.getString(R.string.required))

        // activate/deactivate login button if form contains error or one of the inputs is empty
        canSubmit = !containsError && !invalidInputLength
        toggleButton(binding.registerBtn, canSubmit)
    }

    private fun doPasswordsMatch(): Boolean {
        val unmatchedPasswordsText = "Passwords did not match"
        val password = registerInputs[FORM_INDEXES.PASSWORD.index].text.toString()
        val passwordConfirmation = registerInputs[FORM_INDEXES.PASSWORD_CONFIRMATION.index].text.toString()

        if (password != passwordConfirmation) {
            registerLayouts[FORM_INDEXES.PASSWORD.index].error = unmatchedPasswordsText
            registerLayouts[FORM_INDEXES.PASSWORD_CONFIRMATION.index].error = unmatchedPasswordsText
            toggleButton(binding.registerBtn, false)
            return false
        }

        return true
    }

    private fun executeRegister() {
        if (!canSubmit || !doPasswordsMatch()) return

        // get input texts
        val firstName = registerInputs[FORM_INDEXES.FIRST_NAME.index].text.toString()
        val lastName = registerInputs[FORM_INDEXES.LAST_NAME.index].text.toString()
        val username = registerInputs[FORM_INDEXES.USERNAME.index].text.toString()
        val email = registerInputs[FORM_INDEXES.EMAIL.index].text.toString()
        val password = registerInputs[FORM_INDEXES.PASSWORD.index].text.toString()

        // form body to make HTTP request
        val newUserData = RegisterNewUser(firstName, lastName, username, email, password)
        val registerObserver = registerViewModel.registerUser(newUserData)
        registerObserver.observe(this, { handleRegisterResponse(it) })
    }

    private fun handleRegisterResponse(response: DataWrapper<HTTPResponseModel>) {
        printToast(applicationContext, response.message as String)

        // some error occurred during HTTP request
        if (response.isError as Boolean) {
            return
        }

        Log.d(DEBUG_KEY, response.data.toString())
    }
}