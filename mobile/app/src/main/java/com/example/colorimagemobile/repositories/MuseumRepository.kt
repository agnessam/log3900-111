package com.example.colorimagemobile.repositories

import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.services.users.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MuseumRepository {

    private val httpClient = RetrofitInstance.HTTP

    fun getAllPosts(): MutableLiveData<DataWrapper<ArrayList<MuseumPostModel>>> {
        val teamDrawingsLiveData: MutableLiveData<DataWrapper<ArrayList<MuseumPostModel>>> = MutableLiveData()

        httpClient.getAllPosts(token = "Bearer ${UserService.getToken()}").enqueue(object : Callback<ArrayList<MuseumPostModel>> {
            override fun onResponse(call: Call<ArrayList<MuseumPostModel>>, response: Response<ArrayList<MuseumPostModel>>) {
                if (!response.isSuccessful) {
                    teamDrawingsLiveData.value = DataWrapper(null, "An error occurred while fetching museum's posts!", true)
                    return
                }
                teamDrawingsLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<ArrayList<MuseumPostModel>>, t: Throwable) {
                teamDrawingsLiveData.value = DataWrapper(null, "Sorry, failed to get fetch museum's posts!", true)
            }
        })

        return teamDrawingsLiveData
    }

}