package com.example.colorimagemobile.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val httpClient = RetrofitInstance.HTTP

    fun getUserByToken(token: String): MutableLiveData<DataWrapper<HTTPResponseModel.GetUser>> {
        val userLiveData: MutableLiveData<DataWrapper<HTTPResponseModel.GetUser>> = MutableLiveData()

        httpClient.getUserByToken(token = "Bearer $token").enqueue(object : Callback<HTTPResponseModel.GetUser> {
            override fun onResponse(call: Call<HTTPResponseModel.GetUser>, response: Response<HTTPResponseModel.GetUser>) {
                if (!response.isSuccessful) {
                    userLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }

                val body = response.body() as HTTPResponseModel.GetUser
                if (!body.err.isNullOrEmpty()) {
                    userLiveData.value = DataWrapper(null, body.err, true)
                    return
                }

                // account successfully created
                userLiveData.value = DataWrapper(response.body(), null, false)
            }

            // duplicate username is coming through here
            override fun onFailure(call: Call<HTTPResponseModel.GetUser>, t: Throwable) {
                Log.d(Constants.DEBUG_KEY, "Failed to get user account ${t.message!!}")
                userLiveData.value = DataWrapper(null, "Failed to get User!", true)
            }
        })

        return userLiveData
    }
}