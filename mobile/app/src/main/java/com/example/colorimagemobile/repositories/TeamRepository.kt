package com.example.colorimagemobile.repositories

import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamRepository {

    private val httpClient = RetrofitInstance.HTTP

    fun getAllTeams(): MutableLiveData<DataWrapper<ArrayList<TeamModel>>> {
        val teamsLiveData: MutableLiveData<DataWrapper<ArrayList<TeamModel>>> = MutableLiveData()

        httpClient.getAllTeams(token = "Bearer ${UserService.getToken()}").enqueue(object : Callback<ArrayList<TeamModel>> {
            override fun onResponse(call: Call<ArrayList<TeamModel>>, response: Response<ArrayList<TeamModel>>) {
                if (!response.isSuccessful) {
                    teamsLiveData.value = DataWrapper(null, "An error occurred while fetching all teams!", true)
                    return
                }
                teamsLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<ArrayList<TeamModel>>, t: Throwable) {
                teamsLiveData.value = DataWrapper(null, "Sorry, failed to get fetch teams messages!", true)
            }
        })

        return teamsLiveData
    }
}