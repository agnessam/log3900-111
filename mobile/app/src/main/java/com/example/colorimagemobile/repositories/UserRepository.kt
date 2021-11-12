package com.example.colorimagemobile.repositories

import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserRepository {
    private val httpClient = RetrofitInstance.HTTP
    private lateinit var newProfileDate : UserModel.UpdateUser
    private lateinit var logHistory : UserModel.UpdateLogHistory

    fun getUserByToken(token: String): MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> {
        val userLiveData: MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> = MutableLiveData()

        httpClient.getUserByToken(token = "Bearer $token").enqueue(object : Callback<HTTPResponseModel.UserResponse> {
            override fun onResponse(call: Call<HTTPResponseModel.UserResponse>, response: Response<HTTPResponseModel.UserResponse>) {
                if (!response.isSuccessful) {
                    userLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                val body = response.body() as HTTPResponseModel.UserResponse
                if (!body.err.isNullOrEmpty()) {
                    userLiveData.value = DataWrapper(null, body.err, true)
                    return
                }
                // account successfully created
                userLiveData.value = DataWrapper(response.body(), null, false)
            }
            // duplicate username is coming through here
            override fun onFailure(call: Call<HTTPResponseModel.UserResponse>, t: Throwable) {
                userLiveData.value = DataWrapper(null, "Failed to get User!", true)
            }
        })

        return userLiveData
    }

    // update user profile data
    fun updateUserData(token: String, id: String): MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> {
        newProfileDate =UserService.getNewProfileData()

        val updateLiveData: MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> = MutableLiveData()

        httpClient.updateUser(token = "Bearer $token",id, newProfileDate).enqueue(object :
            Callback<HTTPResponseModel.UserResponse> {
            override fun onResponse(call: Call<HTTPResponseModel.UserResponse>, response: Response<HTTPResponseModel.UserResponse>) {
                if (!response.isSuccessful) {
                    updateLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                // account successfully update
                updateLiveData.value = DataWrapper(response.body(), "", false)
            }

            override fun onFailure(call: Call<HTTPResponseModel.UserResponse>, t: Throwable) {
                updateLiveData.value = DataWrapper(null, "Failed to create account!", true)
            }

        })

        return updateLiveData
    }

    // get user by id
    fun getUserById(token: String,id:String): MutableLiveData<DataWrapper<UserModel.AllInfo>> {
        val userData: MutableLiveData<DataWrapper<UserModel.AllInfo>> = MutableLiveData()

        httpClient.getUserById(token = "Bearer $token",id).enqueue(object : Callback<UserModel.AllInfo> {
            override fun onResponse(call: Call<UserModel.AllInfo>, response: Response<UserModel.AllInfo>) {
                if (!response.isSuccessful) {
                    userData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                userData.value = DataWrapper(response.body(), null, false)
            }
            override fun onFailure(call: Call<UserModel.AllInfo>, t: Throwable) {
                userData.value = DataWrapper(null, "Failed to get User!", true)
            }
        })

        return userData
    }

    // delete user by id
    fun deleteUserById(token: String, id: String): MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> {

        val deleteUserData: MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> = MutableLiveData()
        httpClient.deleteUserById(token = "Bearer $token",id).enqueue(object :
            Callback<HTTPResponseModel.UserResponse> {
            override fun onResponse(call: Call<HTTPResponseModel.UserResponse>, response: Response<HTTPResponseModel.UserResponse>) {
                if (!response.isSuccessful) {
                    deleteUserData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }

                // account successfully delete
                deleteUserData.value = DataWrapper(response.body(), "", false)
            }

            override fun onFailure(call: Call<HTTPResponseModel.UserResponse>, t: Throwable) {
                deleteUserData.value = DataWrapper(null, "Failed to delete account!", true)
            }

        })

        return deleteUserData
    }

    // get all user
    fun getAllUser(token: String): MutableLiveData<DataWrapper<List<UserModel.AllInfo>>> {
        val AllUserData: MutableLiveData<DataWrapper<List<UserModel.AllInfo>>> = MutableLiveData()

        httpClient.getAllUser(token = "Bearer $token").enqueue(object : Callback<List<UserModel.AllInfo>> {
            override fun onResponse(call: Call<List<UserModel.AllInfo>>, response: Response<List<UserModel.AllInfo>>) {
                if (!response.isSuccessful) {
                    AllUserData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                AllUserData.value = DataWrapper(response.body(), null, false)
            }
            override fun onFailure(call: Call<List<UserModel.AllInfo>>, t: Throwable) {
                AllUserData.value = DataWrapper(null, "Failed to get User!", true)
            }
        })

        return AllUserData
    }

    // update login or logout history
    fun updateLogHistoryData(token: String, id: String): MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> {

        logHistory=UserService.getLogHistory()
        val updateLogHistoryData: MutableLiveData<DataWrapper<HTTPResponseModel.UserResponse>> = MutableLiveData()
        httpClient.updateLogHistory(token = "Bearer $token",id, logHistory).enqueue(object :
            Callback<HTTPResponseModel.UserResponse> {
            override fun onResponse(call: Call<HTTPResponseModel.UserResponse>, response: Response<HTTPResponseModel.UserResponse>) {
                if (!response.isSuccessful) {
                    updateLogHistoryData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                // account successfully update
                updateLogHistoryData.value = DataWrapper(response.body(), "", false)
            }

            override fun onFailure(call: Call<HTTPResponseModel.UserResponse>, t: Throwable) {
                updateLogHistoryData.value = DataWrapper(null, "Failed to create account!", true)
            }
        })

        return updateLogHistoryData
    }

}