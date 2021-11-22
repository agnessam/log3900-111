package com.example.colorimagemobile.services.teams

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.repositories.TeamRepository
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast

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

    fun addTeam(team: TeamModel) {
        allTeams.add(team)
    }

    fun updateTeamByPosition(position: Int, newTeam: TeamModel) {
        allTeams[position] = newTeam
    }

    fun isUserAlreadyTeamMember(position: Int): Boolean {
        return allTeams[position].members.contains(UserService.getUserInfo()._id)
    }

    fun removeMemberFromTeam(position: Int) {
        allTeams[position].members = allTeams[position].members.filter { member -> member != UserService.getUserInfo()._id } as ArrayList<String>
    }

    fun joinTeam(position: Int, context: Context) {
        val team = getTeam(position)

        TeamRepository().joinTeam(team._id!!).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                printToast(context, it.message!!)
                return@observe
            }

            val joinedTeam = it.data as TeamModel
            updateTeamByPosition(position, joinedTeam)
            TeamAdapterService.getTeamMenuAdapter().notifyItemChanged(position)
        })
    }

    fun leaveTeam(position: Int, context: Context) {
        val team = getTeam(position)

        TeamRepository().leaveTeam(team._id!!).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                printToast(context, it.message!!)
                return@observe
            }

            removeMemberFromTeam(position)
            TeamAdapterService.getTeamMenuAdapter().notifyItemChanged(position)
        })
    }
}