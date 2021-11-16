package com.example.colorimagemobile.repositories

import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.CreateTeamModel
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

    fun joinTeam(teamId: String): MutableLiveData<DataWrapper<TeamModel>> {
        val joinTeamLiveData: MutableLiveData<DataWrapper<TeamModel>> = MutableLiveData()

        httpClient.joinTeam(token = "Bearer ${UserService.getToken()}", teamId).enqueue(object : Callback<TeamModel> {
            override fun onResponse(call: Call<TeamModel>, response: Response<TeamModel>) {
                if (!response.isSuccessful) {
                    joinTeamLiveData.value = DataWrapper(null, "An error occurred while joining team!", true)
                    return
                }
                joinTeamLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<TeamModel>, t: Throwable) {
                joinTeamLiveData.value = DataWrapper(null, "Sorry, failed to get join team!", true)
            }
        })

        return joinTeamLiveData
    }

    fun createTeam(team: CreateTeamModel): MutableLiveData<DataWrapper<TeamModel>> {
        val createTeamLiveData: MutableLiveData<DataWrapper<TeamModel>> = MutableLiveData()

        httpClient.createNewTeam(token = "Bearer ${UserService.getToken()}", team).enqueue(object : Callback<TeamModel> {
            override fun onResponse(call: Call<TeamModel>, response: Response<TeamModel>) {
                if (!response.isSuccessful) {
                    createTeamLiveData.value = DataWrapper(null, "An error occurred while creating team!", true)
                    return
                }
                createTeamLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<TeamModel>, t: Throwable) {
                createTeamLiveData.value = DataWrapper(null, "Sorry, failed to create team!", true)
            }
        })

        return createTeamLiveData
    }
}