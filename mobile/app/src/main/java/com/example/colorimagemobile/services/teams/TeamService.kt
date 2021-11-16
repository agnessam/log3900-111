package com.example.colorimagemobile.services.teams

import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.services.UserService

object TeamService {
    val JOINED_KEYWORD = "Joined"
    private var allTeams: ArrayList<TeamModel> = arrayListOf()

    fun setAllTeams(newTeams: ArrayList<TeamModel>) {
        allTeams = newTeams
    }

    fun getAllTeams(): ArrayList<TeamModel> {
        return allTeams
    }

    fun getTeam(position: Int): TeamModel {
        return allTeams[position]
    }

    fun addTeam(team: TeamModel) {
        allTeams.add(team)
    }

    fun updateTeamByPosition(position: Int, newTeam: TeamModel) {
        allTeams[position] = newTeam
    }

    fun shouldHideJoinTeamButton(position: Int): Boolean {
        return getTeam(position).members.contains(UserService.getUserInfo()._id)
    }
}