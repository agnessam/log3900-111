package com.example.colorimagemobile.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TextChannelRepository {
    private val httpClient = RetrofitInstance.HTTP

    fun getAllTextChannel(token : String): MutableLiveData<DataWrapper<ArrayList<TextChannelModel.AllInfo>>> {
        printMsg("Fetching all chat channels")
        val ChannelListLiveData: MutableLiveData<DataWrapper<ArrayList<TextChannelModel.AllInfo>>> = MutableLiveData()

        httpClient.getAllTextChannel(token = "Bearer $token").enqueue(object :
            Callback<ArrayList<TextChannelModel.AllInfo>> {
            override fun onResponse(
                call: Call<ArrayList<TextChannelModel.AllInfo>>,
                response: Response<ArrayList<TextChannelModel.AllInfo>>
            ) {
                if (!response.isSuccessful) {
                    ChannelListLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                // channel
                ChannelListLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<ArrayList<TextChannelModel.AllInfo>>, t: Throwable) {
                ChannelListLiveData.value = DataWrapper(null, "Failed to get chat channel!", true)
            }

        })

        return ChannelListLiveData
    }

    // create new channel
    fun addChannel(newChannel: TextChannelModel.CreateChannel): MutableLiveData<DataWrapper<TextChannelModel.AllInfo>> {
        val newChannelData: MutableLiveData<DataWrapper<TextChannelModel.AllInfo>> = MutableLiveData()
        val token = UserService.getToken()
        httpClient.addChannel(token = "Bearer $token",newChannel).enqueue(object : Callback<TextChannelModel.AllInfo> {
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
                newChannelData.value = DataWrapper(null, "Failed to create channel!", true)
            }
        })

        return newChannelData
    }

    // delete channel by id
    fun deleteChannelById(id: String): MutableLiveData<DataWrapper<TextChannelModel.AllInfo>> {
        val token = UserService.getToken()
        val deleteChannelData: MutableLiveData<DataWrapper<TextChannelModel.AllInfo>> = MutableLiveData()
        httpClient.deleteChannelById(token = "Bearer $token",id).enqueue(object :
            Callback<TextChannelModel.AllInfo> {
            override fun onResponse(call: Call<TextChannelModel.AllInfo>, response: Response<TextChannelModel.AllInfo>) {
                if (!response.isSuccessful) {
                    deleteChannelData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                // channel successfully delete
                deleteChannelData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<TextChannelModel.AllInfo>, t: Throwable) {
                deleteChannelData.value = DataWrapper(null, "Failed to delete channel!", true)
            }

        })

        return deleteChannelData
    }

}