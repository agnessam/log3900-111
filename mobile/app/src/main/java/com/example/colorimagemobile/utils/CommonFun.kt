package com.example.colorimagemobile.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.colorimagemobile.LoginActivity

class CommonFun {
    companion object {
        fun printToast(applicationContext: Context, message: String) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }

        fun redirectTo(currentActivity: Activity, destinationClass: Class<*>?) {
            val intent: Intent = Intent(currentActivity, destinationClass)
            currentActivity.startActivity(intent)
            currentActivity.finish()
        }
    }
}