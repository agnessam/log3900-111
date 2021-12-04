package com.example.colorimagemobile.repositories

import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.ChatSocketModel
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TextChannelRepository {
    private val httpClient = RetrofitInstance.HTTP

    fun getTeamChannels(): MutableLiveData<DataWrapper<ArrayList<TextChannelModel.AllInfo>>> {
        val channelListLiveData: MutableLiveData<DataWrapper<ArrayList<TextChannelModel.AllInfo>>> = MutableLiveData()

        httpClient.getTeamChannels(token = "Bearer ${UserService.getToken()}").enqueue(object : Callback<ArrayList<TextChannelModel.AllInfo>> {
            override fun onResponse(call: Call<ArrayList<TextChannelModel.AllInfo>>, response: Response<ArrayList<TextChannelModel.AllInfo>>) {
                if (!response.isSuccessful) {
                    channelListLiveData.value = DataWrapper(null, "An error occurred while loading getting team channels!", true)
                    return
                }
                channelListLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<ArrayList<TextChannelModel.AllInfo>>, t: Throwable) {
                channelListLiveData.value = DataWrapper(null, "Sorry, failed to get team channels!", true)
            }
        })

        return channelListLiveData
    }

    fun getChannelByDrawingId(drawingId: String): MutableLiveData<DataWrapper<TextChannelModel.AllInfo>> {
        val channelListLiveData: MutableLiveData<DataWrapper<TextChannelModel.AllInfo>> = MutableLiveData()

        httpClient.getChannelByDrawingId(token = "Bearer ${UserService.getToken()}", drawingId).enqueue(object : Callback<TextChannelModel.AllInfo> {
            override fun onResponse(call: Call<TextChannelModel.AllInfo>, response: Response<TextChannelModel.AllInfo>) {
                if (!response.isSuccessful) {
                    channelListLiveData.value = DataWrapper(null, "An error occurred while loading getting getChannelByDrawingId!", true)
                    return
                }
                channelListLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<TextChannelModel.AllInfo>, t: Throwable) {
                channelListLiveData.value = DataWrapper(null, "Sorry, failed to get getChannelByDrawingId!", true)
            }
        })

        return channelListLiveData
    }

    fun getTextChannelMessages(channelId: String): MutableLiveData<DataWrapper<ArrayList<ChatSocketModel>>> {
        val channelListLiveData: MutableLiveData<DataWrapper<ArrayList<ChatSocketModel>>> = MutableLiveData()

        httpClient.getAllTextChannelMessages(token = "Bearer ${UserService.getToken()}", channelId).enqueue(object : Callback<ArrayList<ChatSocketModel>> {
            override fun onResponse(call: Call<ArrayList<ChatSocketModel>>, response: Response<ArrayList<ChatSocketModel>>) {
                if (!response.isSuccessful) {
                    channelListLiveData.value = DataWrapper(null, "An error occurred while loading previous messages!", true)
                    return
                }
                channelListLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<ArrayList<ChatSocketModel>>, t: Throwable) {
                channelListLiveData.value = DataWrapper(null, "Sorry, failed to get previous chat messages!", true)
            }
        })

        return channelListLiveData
    }

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
    fun addChannel(newChannel: TextChannelModel.AllInfo): MutableLiveData<DataWrapper<TextChannelModel.AllInfo>> {
        val newChannelData: MutableLiveData<DataWrapper<TextChannelModel.AllInfo>> = MutableLiveData()

        httpClient.addChannel(token = "Bearer ${UserService.getToken()}",newChannel).enqueue(object : Callback<TextChannelModel.AllInfo> {
            override fun onResponse(call: Call<TextChannelModel.AllInfo>, response: Response<TextChannelModel.AllInfo>) {
                if (!response.isSuccessful) {
                    newChannelData.value = DataWrapper(null, "An error occurred while creating new channel!", true)
                    return
                }

                // channel successfully created
                newChannelData.value = DataWrapper(response.body(), "", false)
            }

            override fun onFailure(call: Call<TextChannelModel.AllInfo>, t: Throwable) {
                newChannelData.value = DataWrapper(null, "Sorry, failed to create channel!", true)
            }
        })

        return newChannelData
    }

    // delete channel by id
    fun deleteChannelById(id: String, channelName: String): MutableLiveData<DataWrapper<TextChannelModel.AllInfo>> {
        val deleteChannelData: MutableLiveData<DataWrapper<TextChannelModel.AllInfo>> = MutableLiveData()

        httpClient.deleteChannelById(token = "Bearer ${UserService.getToken()}", id).enqueue(object : Callback<TextChannelModel.AllInfo> {
            override fun onResponse(call: Call<TextChannelModel.AllInfo>, response: Response<TextChannelModel.AllInfo>) {
                if (!response.isSuccessful) {
                    deleteChannelData.value = DataWrapper(null, "An error occurred while deleting channel!", true)
                    return
                }

                // channel successfully delete
                deleteChannelData.value = DataWrapper(response.body(), "Channel \"$channelName\" has successfully been deleted!", false)
            }
            override fun onFailure(call: Call<TextChannelModel.AllInfo>, t: Throwable) {
                deleteChannelData.value = DataWrapper(null, "Sorry, failed to delete channel!", true)
            }
        })

        return deleteChannelData
    }

    fun deleteMessages(id: String): MutableLiveData<DataWrapper<Any>> {
        val deleteChannelData: MutableLiveData<DataWrapper<Any>> = MutableLiveData()

        httpClient.deleteMessages(token = "Bearer ${UserService.getToken()}", id).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (!response.isSuccessful) {
                    deleteChannelData.value = DataWrapper(null, "An error occurred while deleting channel's messages!", true)
                    return
                }

                // channel successfully delete
                deleteChannelData.value = DataWrapper(response.body(), null, false)
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                deleteChannelData.value = DataWrapper(null, "Sorry, failed to delete channel's messages!", true)
            }
        })

        return deleteChannelData
    }

    fun searchChannels(query: String): MutableLiveData<DataWrapper<ArrayList<TextChannelModel.AllInfo>>> {
        val channelsLiveData: MutableLiveData<DataWrapper<ArrayList<TextChannelModel.AllInfo>>> = MutableLiveData()

        httpClient.searchChannels(token = "Bearer ${UserService.getToken()}", query).enqueue(object : Callback<ArrayList<TextChannelModel.AllInfo>> {
            override fun onResponse(call: Call<ArrayList<TextChannelModel.AllInfo>>, response: Response<ArrayList<TextChannelModel.AllInfo>>) {
                if (!response.isSuccessful) {
                    channelsLiveData.value = DataWrapper(null, "An error occurred while fetching channels!", true)
                    return
                }
                channelsLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<ArrayList<TextChannelModel.AllInfo>>, t: Throwable) {
                channelsLiveData.value = DataWrapper(null, "Sorry, failed to get fetch channels!", true)
            }
        })

        return channelsLiveData
    }
}