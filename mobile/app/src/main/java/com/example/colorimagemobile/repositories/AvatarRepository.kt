package com.example.colorimagemobile.repositories

import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.AvatarModel
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.services.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AvatarRepository {
    private val httpClient = RetrofitInstance.HTTP

    // get all avatar
    fun getAllAvatar(token: String): MutableLiveData<DataWrapper<ArrayList<AvatarModel.AllInfo>>> {
        val AllAvatarData: MutableLiveData<DataWrapper<ArrayList<AvatarModel.AllInfo>>> = MutableLiveData()

        httpClient.getAllAvatar(token = "Bearer $token").enqueue(object :
            Callback<ArrayList<AvatarModel.AllInfo>> {
            override fun onResponse(call: Call<ArrayList<AvatarModel.AllInfo>>, response: Response<ArrayList<AvatarModel.AllInfo>>) {
                if (!response.isSuccessful) {
                    AllAvatarData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                AllAvatarData.value = DataWrapper(response.body(), null, false)
            }
            override fun onFailure(call: Call<ArrayList<AvatarModel.AllInfo>>, t: Throwable) {
                AllAvatarData.value = DataWrapper(null, "Failed to fetch all avatar from database!", true)
            }
        })

        return AllAvatarData
    }
}