package com.example.colorimagemobile.repositories

import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DrawingRepository {

    private val httpClient = RetrofitInstance.HTTP

    fun getAllDrawings(token: String): MutableLiveData<DataWrapper<List<DrawingModel.Drawing>>> {
        val liveData: MutableLiveData<DataWrapper<List<DrawingModel.Drawing>>> = MutableLiveData()

        httpClient.getAllDrawings(token = "Bearer $token").enqueue(object : Callback<List<DrawingModel.Drawing>> {
            override fun onResponse(call: Call<List<DrawingModel.Drawing>>, response: Response<List<DrawingModel.Drawing>>) {
                if (!response.isSuccessful) {
                    liveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }

                liveData.value = DataWrapper(response.body(), null, false)
            }

            // duplicate username is coming through here
            override fun onFailure(call: Call<List<DrawingModel.Drawing>>, t: Throwable) {
                printMsg("Failed to get all drawings ${t.message!!}")
                liveData.value = DataWrapper(null, "Failed to get all drawings!", true)
            }
        })

        return liveData
    }
}