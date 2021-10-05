package com.example.colorimagemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.LoginResponseModel
import com.example.colorimagemobile.models.User
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.ui.login.LoginActivityViewModel
import com.example.colorimagemobile.utils.CommonFun.Companion.closeKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.onEnterKeyPressed
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_TOKEN_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_USERNAME_KEY

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginActivityViewModel
    private lateinit var sharedPreferencesService: SharedPreferencesService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)
        sharedPreferencesService = SharedPreferencesService(this)

        setListeners()
    }

    private fun setListeners() {
        val loginBtn: Button = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener { executeLogin() }

        val editText: EditText = findViewById(R.id.usernameInput)
        onEnterKeyPressed(editText) { executeLogin() }

        val loginMain: ConstraintLayout = findViewById(R.id.loginMain)
        loginMain.setOnTouchListener { v, event -> closeKeyboard(this) }
    }

    private fun executeLogin() {
        val usernameInput: TextView = findViewById(R.id.usernameInput)
        val user = User(usernameInput.text.toString(), "kesh")

        // check if input is valid
        if (user.username.trim().length === 0) {
            printToast(applicationContext, "Error! Please enter a valid username!")
            return
        }

        // username ok -> make HTTP POST request
        val loginObserver = loginViewModel.loginUser(user)
        loginObserver.observe(this, { handleLoginResponse(it) })
    }

    // response from HTTP request
    private fun handleLoginResponse(it: DataWrapper<LoginResponseModel>) {
        printToast(applicationContext, it.message as String)

        // some error occurred during HTTP request
        if (it.isError as Boolean) {
            return
        }

        val userResponse = it.data as LoginResponseModel
        val token = userResponse.token.toString()
        val username = userResponse.username.toString()

        // save credentials to "local storage"
        sharedPreferencesService.setItem(SHARED_USERNAME_KEY, username)
        sharedPreferencesService.setItem(SHARED_TOKEN_KEY, token)

        // redirect to /chat
        redirectTo(this@LoginActivity, ChatActivity::class.java)
    }
}