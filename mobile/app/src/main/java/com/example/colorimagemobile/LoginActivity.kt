package com.example.colorimagemobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

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

    fun setLoginListener() {
        val loginBtn: Button = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener(View.OnClickListener { view ->

            // get username and error element
            val usernameInput: TextView = findViewById(R.id.usernameInput)
            val errorElement: TextView = findViewById(R.id.errorText)

            // check if input is valid
            if (usernameInput.text.length === 0) {
                errorElement.text = "Error! Please enter a valid username!"
            }

            // make HTTP request to update users

            // redirect to chatroom page
            val intent = Intent(this, ChatRoomActivity::class.java).apply {
                putExtra("username", usernameInput.text)
            }
            startActivity(intent)
        })
    }
}