package com.example.colorimagemobile.services.teams

import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.adapter.TeamsMenuRecyclerAdapter
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.ui.home.fragments.teams.TeamsProfileFragment
import com.example.colorimagemobile.utils.Constants

object TeamAdapterService {

    private lateinit var teamMenuAdapter: TeamsMenuRecyclerAdapter

    fun createAdapter(fragmentActivity: FragmentActivity, layoutID: Int, parentFragmentID: Int): TeamsMenuRecyclerAdapter {
        return TeamsMenuRecyclerAdapter(
            layoutID
        ) { teamId -> openTeam(fragmentActivity, teamId, parentFragmentID) }
    }

    fun getTeamMenuAdapter(): TeamsMenuRecyclerAdapter {
        return teamMenuAdapter
    }

    fun setAdapter(adapter: TeamsMenuRecyclerAdapter) {
        teamMenuAdapter = adapter
    }

    private fun openTeam(fragmentActivity: FragmentActivity, teamId: String, parentFragmentID: Int) {
        MyFragmentManager(fragmentActivity).openWithData(parentFragmentID, TeamsProfileFragment(), Constants.TEAMS.CURRENT_TEAM_ID_KEY, teamId)
    }
}