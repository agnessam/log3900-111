package com.example.colorimagemobile.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.services.chat.ChatChannelService.getAllChatInfo
import com.example.colorimagemobile.services.chat.ChatChannelService.getAllChatInfoName
import com.example.colorimagemobile.services.chat.ChatChannelService.setAllChatInfo
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatChannelRepository {
    private val httpClient = RetrofitInstance.HTTP

    fun getAllChatChannel(token : String): MutableLiveData<DataWrapper<List<HTTPResponseModel.GetChannelList>>> {
        val ChannelListLiveData: MutableLiveData<DataWrapper<List<HTTPResponseModel.GetChannelList>>> = MutableLiveData()

        httpClient.getAllChatChannel(token = "Bearer $token").enqueue(object :
            Callback<List<HTTPResponseModel.GetChannelList>> {
            override fun onResponse(
                call: Call<List<HTTPResponseModel.GetChannelList>>,
                response: Response<List<HTTPResponseModel.GetChannelList>>
            ) {
                printMsg("response.body  inside onresponse================================================= "+response.body())

                if (!response.isSuccessful) {
                    ChannelListLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }

                val body: List<HTTPResponseModel.GetChannelList> = response.body()!!

                setAllChatInfo(body)

//                for (post in body) {
//                    if (post.err.isNullOrEmpty()){
//                        ChannelListLiveData.value = DataWrapper(null, post.err, true)
//                        printMsg("inside poat.err is empty ================================================= "+post.channelInfo)
//                        return
//                    }
//                }

                // channel
                ChannelListLiveData.value = DataWrapper(response.body(), "", false)

                printMsg("response.body ================================================= "+response.body())
                printMsg("on success getchatinfo =" +getAllChatInfoName())


            }
            override fun onFailure(call: Call<List<HTTPResponseModel.GetChannelList>>, t: Throwable) {
                Log.d(Constants.DEBUG_KEY, "Failed to get all channel ${t.message!!}")
                ChannelListLiveData.value = DataWrapper(null, "Failed to get chat channel!", true)
            }

        })

        return ChannelListLiveData
    }



    }