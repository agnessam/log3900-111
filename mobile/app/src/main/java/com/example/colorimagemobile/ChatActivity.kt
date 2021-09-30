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
import com.example.colorimagemobile.model.User
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.Constants
import com.example.colorimagemobile.utils.Constants.Companion.LOCAL_STORAGE_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_TOKEN_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_USERNAME_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatActivity : AppCompatActivity() {
    var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val sharedPref = getSharedPreferences(Constants.LOCAL_STORAGE_KEY, Context.MODE_PRIVATE)
        val token = sharedPref.getString(SHARED_TOKEN_KEY, "")
        this.username = sharedPref.getString(SHARED_USERNAME_KEY, "") as String

        // check if token is valid! if not, redirect to /login
        if (token == "" || token == null) {
            redirectTo(this@ChatActivity, LoginActivity::class.java)
        }

        // update chat text by showing username
        val chatText: TextView = findViewById(R.id.chatText)
        chatText.append(" ${this.username}")

        setLogOutListener()
    }

    private fun setLogOutListener() {
        val logOutBtn: Button = findViewById(R.id.logoutBtn)

        logOutBtn.setOnClickListener(View.OnClickListener {
            // log out -> POST /logout
            val user = User(this.username, "kesh")

            RetrofitInstance.HTTP.logoutUser(user).enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (!response.isSuccessful) {
                        Log.d("HTTP request error", response.message())
                        printToast(applicationContext, "An error occurred!")
                        return
                    }

                    printToast(applicationContext, "Logging you out ${user.username}!")

                    // remove items from "local storage"
                    val sharedPref = getSharedPreferences(LOCAL_STORAGE_KEY, Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.clear().apply()

                    // redirect to /login
                    redirectTo(this@ChatActivity, LoginActivity::class.java)
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.d("User failed to log out", t.message!!)
                    printToast(applicationContext, "Failed to logout!\nAn error occurred")
                }
            })
        })
    }
}