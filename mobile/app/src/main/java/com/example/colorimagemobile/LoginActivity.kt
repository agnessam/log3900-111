package com.example.colorimagemobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.colorimagemobile.handler.RetrofitInstance
import com.example.colorimagemobile.model.LoginResponse
import com.example.colorimagemobile.model.User
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.Constants.Companion.LOCAL_STORAGE_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_TOKEN_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_USERNAME_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //setSignUpListener()
        setLoginListener()

    }

    fun setSignUpListener() {
        // sign up listener -> redirect to sign up page
//        val signUpBtn: TextView = findViewById(R.id.signUpLink)
//        signUpBtn.setOnClickListener(View.OnClickListener { view ->
//            startActivity(Intent(this, SignUpActivity::class.java))
//            finish()
//        })
    }

    private fun setLoginListener() {
        val loginBtn: Button = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener(View.OnClickListener {

            val usernameInput: TextView = findViewById(R.id.usernameInput)

            // check if input is valid
            if (usernameInput.text.length === 0) {
                printToast(applicationContext, "Error! Please enter a valid username!")
                return@OnClickListener
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

            // redirect to chatroom page
            val username = usernameInput.toString()
            startActivity<ChatRoomActivity>("username" to username)
        })
    }
}