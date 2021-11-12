package com.example.colorimagemobile.repositories

import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.MessageModel
import com.example.colorimagemobile.services.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageRepository {

    private val httpClient = RetrofitInstance.HTTP

    // get all Messages
    fun getAllMessage(token: String): MutableLiveData<DataWrapper<List<MessageModel.AllInfo>>> {
        val AllMessageData: MutableLiveData<DataWrapper<List<MessageModel.AllInfo>>> = MutableLiveData()

        httpClient.getAllMessage(token = "Bearer $token").enqueue(object :
            Callback<List<MessageModel.AllInfo>> {
            override fun onResponse(call: Call<List<MessageModel.AllInfo>>, response: Response<List<MessageModel.AllInfo>>) {
                if (!response.isSuccessful) {
                    AllMessageData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                AllMessageData.value = DataWrapper(response.body(), null, false)
            }
            override fun onFailure(call: Call<List<MessageModel.AllInfo>>, t: Throwable) {
                AllMessageData.value = DataWrapper(null, "Failed to get Message!", true)
            }
        })

        return AllMessageData
    }
}