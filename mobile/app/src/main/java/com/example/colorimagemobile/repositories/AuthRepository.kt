package com.example.colorimagemobile.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.classes.User
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.utils.Constants.Companion.DEBUG_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository {

    private val httpClient = RetrofitInstance.HTTP
    private val authLiveData: MutableLiveData<DataWrapper<HTTPResponseModel>>

    init {
        authLiveData = MutableLiveData()
    }

    fun loginUser(user: User.Login): MutableLiveData<DataWrapper<HTTPResponseModel>> {
        httpClient.loginUser(user).enqueue(object : Callback<HTTPResponseModel> {
            override fun onResponse(call: Call<HTTPResponseModel>, response: Response<HTTPResponseModel>) {
                if (!response.isSuccessful) {
                    Log.d(DEBUG_KEY, response.message())
                    authLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }

                // user logged in successfully
                authLiveData.value = DataWrapper(response.body(), "Login Successful", false)
            }

            // duplicate username is coming through here
            override fun onFailure(call: Call<HTTPResponseModel>, t: Throwable) {
                Log.d(DEBUG_KEY, "User failed to login ${t.message!!}")
                authLiveData.value = DataWrapper(null, "Username possibly already exists!", true)
            }
        })

        return authLiveData
    }

    // when user logs out
    fun logoutUser(user: User.Logout): MutableLiveData<DataWrapper<HTTPResponseModel>>  {
        httpClient.logoutUser(user).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (!response.isSuccessful) {
                    Log.d(DEBUG_KEY, response.message())
                    authLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }

                authLiveData.value = DataWrapper(null, "Logging you out ${user.username}!", false)
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(DEBUG_KEY,"User failed to log out ${t.message!!}")
                authLiveData.value = DataWrapper(null, "Failed to logout!\nAn error occurred", true)
            }
        })

        return authLiveData
    }

    // create new user: TO CHANGE
    fun registerUser(newUserData: User.Register): MutableLiveData<DataWrapper<HTTPResponseModel>> {
        httpClient.registerUser(newUserData).enqueue(object : Callback<HTTPResponseModel> {
            override fun onResponse(call: Call<HTTPResponseModel>, response: Response<HTTPResponseModel>) {
                if (!response.isSuccessful) {
                    Log.d(DEBUG_KEY, response.message())
                    authLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }

                // account successfully created
                authLiveData.value = DataWrapper(response.body(), "Account successfully created", false)
            }

            // duplicate username is coming through here
            override fun onFailure(call: Call<HTTPResponseModel>, t: Throwable) {
                Log.d(DEBUG_KEY, "Failed to create account ${t.message!!}")
                authLiveData.value = DataWrapper(null, "Failed to create account!", true)
            }
        })

        return authLiveData
    }
}
