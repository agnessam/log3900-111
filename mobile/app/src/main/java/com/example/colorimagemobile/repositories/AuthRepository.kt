package com.example.colorimagemobile.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.LoginActivity
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.LoginResponseModel
import com.example.colorimagemobile.models.User
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository {

    private val httpClient = RetrofitInstance.HTTP
    private val authLiveData: MutableLiveData<DataWrapper<LoginResponseModel>>

    init {
        authLiveData = MutableLiveData()
    }

    fun loginUser(user: User): MutableLiveData<DataWrapper<LoginResponseModel>> {
        httpClient.loginUser(user).enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                if (!response.isSuccessful) {
                    Log.d("HTTP request error", response.message())
                    authLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }

                // user logged in successfully
                authLiveData.value = DataWrapper(response.body(), "Login Successful", false)
            }

            // duplicate username is coming through here
            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                Log.d("User failed to login", t.message!!)
                authLiveData.value = DataWrapper(null, "Username possibly already exists!", true)
            }
        })

        return authLiveData
    }

    // when user logs out
    fun logoutUser(user: User): MutableLiveData<DataWrapper<LoginResponseModel>>  {
        httpClient.logoutUser(user).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (!response.isSuccessful) {
                    Log.d("HTTP request error", response.message())
                    authLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }

                authLiveData.value = DataWrapper(null, "Logging you out ${user.username}!", false)
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d("User failed to log out", t.message!!)
                authLiveData.value = DataWrapper(null, "Failed to logout!\nAn error occurred", true)
            }
        })

        return authLiveData
    }
}
