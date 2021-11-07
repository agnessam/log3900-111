package com.example.colorimagemobile.services

import android.content.Context
import android.content.SharedPreferences
import com.example.colorimagemobile.utils.Constants

class SharedPreferencesService(context: Context) {

    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(Constants.STORAGE_KEY.MAIN, Context.MODE_PRIVATE)
    }

    // saves an item with a key to localStorage
    fun setItem(key: String, item: String) {
        if (key.isNullOrEmpty()) return

        val editor = sharedPreferences.edit()
        editor.apply {
            putString(key, item)
        }.apply()
    }

    // retrieves an item from localStorage by key
    fun getItem(key: String): String {
        return sharedPreferences.getString(key, "").toString()
    }

    // removes a specific key from localStorage
    fun removeItem(key: String) {
        if (key.isNullOrEmpty()) return

        val editor = sharedPreferences.edit()
        editor.apply {
            remove(key)
        }.apply()
    }

    // removes everything from localStorage
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear().apply()
    }
}