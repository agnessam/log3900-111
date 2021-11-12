package com.example.colorimagemobile.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.services.RetrofitInstance
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
                Log.d(Constants.DEBUG_KEY, "Failed to get all channel ${t.message!!}")
                ChannelListLiveData.value = DataWrapper(null, "Failed to get chat channel!", true)
            }

        })

        return ChannelListLiveData
    }
//
//    fun getChannelByid(token: String,id: String): MutableLiveData<DataWrapper<HTTPResponseModel.TextChannelResponse>> {
//        val cHannelData: MutableLiveData<DataWrapper<HTTPResponseModel.TextChannelResponse>> = MutableLiveData()
//
//        httpClient.getChannelByid(token = "Bearer $token",id).enqueue(object : Callback<HTTPResponseModel.TextChannelResponse> {
//            override fun onResponse(call: Call<HTTPResponseModel.TextChannelResponse>, response: Response<HTTPResponseModel.TextChannelResponse>) {
//                if (!response.isSuccessful) {
//                    cHannelData.value = DataWrapper(null, "An error occurred!", true)
//                    return
//                }
//
//                val body = response.body() as HTTPResponseModel.TextChannelResponse
//                if (!body.err.isNullOrEmpty()) {
//                    cHannelData.value = DataWrapper(null, body.err, true)
//                    return
//                }
//
//
//                cHannelData.value = DataWrapper(response.body(), null, false)
//            }
//
//            override fun onFailure(call: Call<HTTPResponseModel.TextChannelResponse>, t: Throwable) {
//                Log.d(Constants.DEBUG_KEY, "Failed to get user account ${t.message!!}")
//                cHannelData.value = DataWrapper(null, "Failed to get User!", true)
//            }
//        })
//
//        return cHannelData
//    }


    }