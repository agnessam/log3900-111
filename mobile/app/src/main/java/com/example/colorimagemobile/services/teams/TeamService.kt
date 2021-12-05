package com.example.colorimagemobile.services.teams

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.models.UpdateTeam
import com.example.colorimagemobile.repositories.TeamRepository
import com.example.colorimagemobile.services.chat.ChatService
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.ui.home.fragments.teams.TeamsMenuFragment
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast

object TeamService {
    private var allTeams: ArrayList<TeamModel> = arrayListOf()
    var showProtectedTeams = false

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
        // if showing protected teams, only add to all teams if team is private
        if (!showProtectedTeams || (showProtectedTeams && team.isPrivate)) {
            allTeams.add(team)
        }
    }

    fun updateTeamByPosition(position: Int, newTeam: TeamModel) {
        allTeams[position] = newTeam
    }

    fun removeMemberFromTeam(position: Int) {
        allTeams[position].members = allTeams[position].members.filter { member -> member != UserService.getUserInfo()._id } as ArrayList<String>
    }

    fun joinTeam(teamId: String, context: Context) {
        TeamRepository().joinTeam(teamId).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                return@observe
            }
        })
    }

    fun leaveTeam(teamId: String, context: Context) {
        TeamRepository().leaveTeam(teamId).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                return@observe
            }
        })
    }

    fun deleteTeam(teamId: String, context: Context) {
        TeamRepository().deleteTeam(teamId).observe(context as LifecycleOwner, {
            printToast(context, it.message!!)

            if (it.isError as Boolean) { return@observe }
            MyFragmentManager(context as FragmentActivity).open(R.id.teamsMenuFrameLayout, TeamsMenuFragment())
            TextChannelService.removeChannel(allTeams.find { team -> team._id == teamId }?.name)
        })
    }

    fun updateTeam(teamId: String, context: Context, newTeamData: UpdateTeam) {
        TeamRepository().updateTeam(teamId, newTeamData).observe(context as LifecycleOwner, {
            printToast(context, it.message!!)
        })
    }
}