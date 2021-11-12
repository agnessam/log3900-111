package com.example.colorimagemobile.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.MessageModel
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.utils.Constants
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

    // send new message
    fun sendMessage(newChannel: MessageModel.SendMessage): MutableLiveData<DataWrapper<MessageModel.SendMessage>> {
        val newChannelData: MutableLiveData<DataWrapper<MessageModel.SendMessage>> = MutableLiveData()
        val token = UserService.getToken()
        httpClient.sendMessage(token = "Bearer $token",newChannel).enqueue(object : Callback<MessageModel.SendMessage> {
            override fun onResponse(call: Call<MessageModel.SendMessage>, response: Response<MessageModel.SendMessage>) {
                if (!response.isSuccessful) {
                    Log.d(Constants.DEBUG_KEY, response.message())
                    newChannelData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                // message send successfully
                newChannelData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<MessageModel.SendMessage>, t: Throwable) {
                newChannelData.value = DataWrapper(null, "Failed to create message!", true)
            }
        })

        return newChannelData
    }
}