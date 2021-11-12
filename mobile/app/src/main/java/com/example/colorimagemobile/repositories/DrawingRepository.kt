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

    fun createNewDrawing(token: String, drawing: DrawingModel.CreateDrawing): MutableLiveData<DataWrapper<DrawingModel.CreateDrawing>> {
        val drawingLiveData: MutableLiveData<DataWrapper<DrawingModel.CreateDrawing>> = MutableLiveData()

        httpClient.createNewDrawing(token = "Bearer $token", drawing).enqueue(object : Callback<DrawingModel.CreateDrawing>{
            override fun onResponse(call: Call<DrawingModel.CreateDrawing>, response: Response<DrawingModel.CreateDrawing>) {
                if (!response.isSuccessful) {
                    drawingLiveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                drawingLiveData.value = DataWrapper(response.body(), "", false)
            }

            override fun onFailure(call: Call<DrawingModel.CreateDrawing>, t: Throwable) {
                drawingLiveData.value = DataWrapper(null, "Failed to create new drawing!", true)
            }
        })

        return drawingLiveData
    }

    fun getAllDrawings(token: String): MutableLiveData<DataWrapper<List<DrawingModel.Drawing>>> {
        val liveData: MutableLiveData<DataWrapper<List<DrawingModel.Drawing>>> = MutableLiveData()
        printMsg("Fetching all drawings")

        httpClient.getAllDrawings(token = "Bearer $token").enqueue(object : Callback<List<DrawingModel.Drawing>> {
            override fun onResponse(call: Call<List<DrawingModel.Drawing>>, response: Response<List<DrawingModel.Drawing>>) {
                if (!response.isSuccessful) {
                    liveData.value = DataWrapper(null, "An error occurred!", true)
                    return
                }
                liveData.value = DataWrapper(response.body(), null, false)
            }

            override fun onFailure(call: Call<List<DrawingModel.Drawing>>, t: Throwable) {
                printMsg("Failed to get all drawings ${t.message!!}")
                liveData.value = DataWrapper(null, "Failed to get all drawings!", true)
            }
        })

        return liveData
    }
}