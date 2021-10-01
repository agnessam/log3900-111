package com.example.colorimagemobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.colorimagemobile.handler.RetrofitInstance
import com.example.colorimagemobile.model.LoginResponse
import com.example.colorimagemobile.model.User
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.closeKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.Constants.Companion.LOCAL_STORAGE_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_TOKEN_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_USERNAME_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn: Button = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener { executeLogin() }

        val editText: EditText = findViewById(R.id.usernameInput)
        editText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                executeLogin()
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })

        val loginMain: ConstraintLayout = findViewById(R.id.loginMain)
        loginMain.setOnTouchListener { v, event ->
            closeKeyboard(this)
        }
    }

    fun setSignUpListener() {
        // sign up listener -> redirect to sign up page
//        val signUpBtn: TextView = findViewById(R.id.signUpLink)
//        signUpBtn.setOnClickListener(View.OnClickListener { view ->
//            startActivity(Intent(this, SignUpActivity::class.java))
//            finish()
//        })
    }

    private fun executeLogin() {
        val usernameInput: TextView = findViewById(R.id.usernameInput)

        // check if input is valid
        if (usernameInput.text.length === 0) {
            printToast(applicationContext, "Error! Please enter a valid username!")
            return
        }

        val user = User(usernameInput.text.toString(), "kesh")

        // username ok -> make HTTP POST request
        RetrofitInstance.HTTP.loginUser(user).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (!response.isSuccessful) {
                    Log.d("HTTP request error", response.message())
                    printToast(applicationContext, "An error occurred!")
                    return
                }

                // new user
                printToast(applicationContext, "Login successful!")
                val token = response.body()?.token.toString()
                val username = response.body()?.username.toString()

                // save credentials to "local storage"
                val sharedPref = getSharedPreferences(LOCAL_STORAGE_KEY, Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.apply {
                    putString(SHARED_USERNAME_KEY, username)
                    putString(SHARED_TOKEN_KEY, token)
                }.apply()

                // redirect to /chat
                redirectTo(this@LoginActivity, ChatActivity::class.java)
            }

            // duplicate username is coming through here
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("User failed to login", t.message!!)
                printToast(applicationContext, "Username possibly already exists!")
            }
        })

    }
}