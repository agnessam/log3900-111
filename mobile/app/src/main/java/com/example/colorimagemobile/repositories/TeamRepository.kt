package com.example.colorimagemobile.repositories

import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.services.RetrofitInstance
import com.example.colorimagemobile.services.users.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamRepository {

    private val httpClient = RetrofitInstance.HTTP

    fun getTeamById(id: String): MutableLiveData<DataWrapper<TeamIdModel>> {
        val teamDrawingsLiveData: MutableLiveData<DataWrapper<TeamIdModel>> = MutableLiveData()

        httpClient.getTeamById(token = "Bearer ${UserService.getToken()}", id).enqueue(object : Callback<TeamIdModel> {
            override fun onResponse(call: Call<TeamIdModel>, response: Response<TeamIdModel>) {
                if (!response.isSuccessful) {
                    teamDrawingsLiveData.value = DataWrapper(null, "An error occurred while fetching team!", true)
                    return
                }
                teamDrawingsLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<TeamIdModel>, t: Throwable) {
                teamDrawingsLiveData.value = DataWrapper(null, "Sorry, failed to get fetch team!", true)
            }
        })

        return teamDrawingsLiveData
    }

    fun getTeamDrawings(id: String): MutableLiveData<DataWrapper<List<DrawingModel.Drawing>>> {
        val teamDrawingsLiveData: MutableLiveData<DataWrapper<List<DrawingModel.Drawing>>> = MutableLiveData()

        httpClient.getTeamDrawings(token = "Bearer ${UserService.getToken()}", id).enqueue(object : Callback<List<DrawingModel.Drawing>> {
            override fun onResponse(call: Call<List<DrawingModel.Drawing>>, response: Response<List<DrawingModel.Drawing>>) {
                if (!response.isSuccessful) {
                    teamDrawingsLiveData.value = DataWrapper(null, "An error occurred while fetching teams' drawings!", true)
                    return
                }
                teamDrawingsLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<List<DrawingModel.Drawing>>, t: Throwable) {
                teamDrawingsLiveData.value = DataWrapper(null, "Sorry, failed to get fetch team's drawings!", true)
            }
        })

        return teamDrawingsLiveData
    }

    fun getTeamPosts(id: String): MutableLiveData<DataWrapper<List<PublishedMuseumPostModel>>> {
        val teamDrawingsLiveData: MutableLiveData<DataWrapper<List<PublishedMuseumPostModel>>> = MutableLiveData()

        httpClient.getTeamPosts(token = "Bearer ${UserService.getToken()}", id).enqueue(object : Callback<List<PublishedMuseumPostModel>> {
            override fun onResponse(call: Call<List<PublishedMuseumPostModel>>, response: Response<List<PublishedMuseumPostModel>>) {
                if (!response.isSuccessful) {
                    teamDrawingsLiveData.value = DataWrapper(null, "An error occurred while fetching team's posts!", true)
                    return
                }
                teamDrawingsLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<List<PublishedMuseumPostModel>>, t: Throwable) {
                teamDrawingsLiveData.value = DataWrapper(null, "Sorry, failed to get fetch team's posts!", true)
            }
        })

        return teamDrawingsLiveData
    }

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

    fun leaveTeam(teamId: String): MutableLiveData<DataWrapper<TeamModel>> {
        val joinTeamLiveData: MutableLiveData<DataWrapper<TeamModel>> = MutableLiveData()

        httpClient.leaveTeam(token = "Bearer ${UserService.getToken()}", teamId).enqueue(object : Callback<TeamModel> {
            override fun onResponse(call: Call<TeamModel>, response: Response<TeamModel>) {
                if (!response.isSuccessful) {
                    joinTeamLiveData.value = DataWrapper(null, "An error occurred while leaving team!", true)
                    return
                }
                joinTeamLiveData.value = DataWrapper(response.body(), "", false)
            }
            override fun onFailure(call: Call<TeamModel>, t: Throwable) {
                joinTeamLiveData.value = DataWrapper(null, "Sorry, failed to leave team!", true)
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
                createTeamLiveData.value = DataWrapper(response.body(), "Team successfully created", false)
            }
            override fun onFailure(call: Call<TeamModel>, t: Throwable) {
                createTeamLiveData.value = DataWrapper(null, "Sorry, failed to create team!", true)
            }
        })

        return createTeamLiveData
    }


    fun deleteTeam(teamId: String): MutableLiveData<DataWrapper<Any>> {
        val joinTeamLiveData: MutableLiveData<DataWrapper<Any>> = MutableLiveData()

        httpClient.deleteTeam(token = "Bearer ${UserService.getToken()}", teamId).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (!response.isSuccessful) {
                    joinTeamLiveData.value = DataWrapper(null, "An error occurred while deleting team!", true)
                    return
                }
                joinTeamLiveData.value = DataWrapper(response.body(), "Team successfully deleted", false)
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                joinTeamLiveData.value = DataWrapper(null, "Sorry, failed to delete team!", true)
            }
        })

        return joinTeamLiveData
    }

}