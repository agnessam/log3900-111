package com.example.colorimagemobile.services.teams

import com.example.colorimagemobile.models.TeamModel

object TeamService {

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

    fun updateTeamByPosition(position: Int, newTeam: TeamModel) {
        allTeams[position] = newTeam
    }
}