package com.example.colorimagemobile.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TextChannelRepository {
    private val httpClient = RetrofitInstance.HTTP

    fun getAllTextChannel(token : String): MutableLiveData<DataWrapper<List<TextChannelModel.AllInfo>>> {
        val ChannelListLiveData: MutableLiveData<DataWrapper<List<TextChannelModel.AllInfo>>> = MutableLiveData()

        httpClient.getAllTextChannel(token = "Bearer $token").enqueue(object :
            Callback<List<TextChannelModel.AllInfo>> {
            override fun onResponse(
                call: Call<List<TextChannelModel.AllInfo>>,
                response: Response<List<TextChannelModel.AllInfo>>
            ) {
                if (!response.isSuccessful) {
                    ChannelListLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                // channel
                ChannelListLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<List<TextChannelModel.AllInfo>>, t: Throwable) {
                ChannelListLiveData.value = DataWrapper(null, "Failed to get chat channel!", true)
            }

        })

        return ChannelListLiveData
    }

    // create new user
    fun addChannel(newChannel: TextChannelModel.CreateChannel): MutableLiveData<DataWrapper<TextChannelModel.AllInfo>> {
        val newChannelData: MutableLiveData<DataWrapper<TextChannelModel.AllInfo>> = MutableLiveData()

        httpClient.addChannel(newChannel).enqueue(object : Callback<TextChannelModel.AllInfo> {
            override fun onResponse(call: Call<TextChannelModel.AllInfo>, response: Response<TextChannelModel.AllInfo>) {
                if (!response.isSuccessful) {
                    Log.d(Constants.DEBUG_KEY, response.message())
                    newChannelData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }

                // channel successfully created
                newChannelData.value = DataWrapper(response.body(), "", false)
            }

            override fun onFailure(call: Call<TextChannelModel.AllInfo>, t: Throwable) {
                newChannelData.value = DataWrapper(null, "Failed to create account!", true)
            }
        })

        return newChannelData
    }


    }