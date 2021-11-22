package com.example.colorimagemobile.utils

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.utils.Constants.Companion.DEBUG_KEY

class CommonFun {
    companion object {
        // print in the terminal with the tag: DEBUG
        fun printMsg(msg: String) {
            Log.d(DEBUG_KEY, msg)
        }

        // display snackbar/message at the bottom of the app
        fun printToast(applicationContext: Context, message: String) {
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }

        fun removeWhitespace(value: String): String {
            return value.replace("\\s".toRegex(), "")
        }

        // close Activity and start another one
        fun redirectTo(currentActivity: Activity, destinationClass: Class<*>?) {
            val intent: Intent = Intent(currentActivity, destinationClass)
            currentActivity.startActivity(intent)
            currentActivity.finish()
        }

        // close/hide Android keyboard
        fun closeKeyboard(currentActivity: Activity): Boolean {
            return try {
                val inputMethodManager =
                    currentActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentActivity.currentFocus!!.windowToken, 0)
                true
            } catch (e: Exception) {
                false
            }
        }

        // close/hide Android keyboard
        fun closeKeyboard_(currentActivity: FragmentActivity): Boolean {
            return try {
                val inputMethodManager =
                    currentActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentActivity.currentFocus!!.windowToken, 0)
                true
            } catch (e: Exception) {
                false
            }
        }

        // when pressed on Enter key, execute callback function
        fun onEnterKeyPressed(editText: EditText, callback: () -> Unit) {
            editText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    callback()
                    return@OnKeyListener true
                }
                return@OnKeyListener false
            })
        }

        // when pressed on Enter key, execute callback function
        fun onEnterKeyPressed_(textview: TextView, callback: () -> Unit) {
            textview.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    callback()
                    return@OnKeyListener true
                }
                return@OnKeyListener false
            })
        }

        // enables or disables button
        fun toggleButton(button: Button, shouldEnable: Boolean) {
            button.alpha = if (shouldEnable) 1f else .4f
            button.isClickable = shouldEnable
            button.isEnabled = shouldEnable
        }
    }
}