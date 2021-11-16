package com.example.colorimagemobile.services.teams

import com.example.colorimagemobile.adapter.TeamsMenuRecyclerAdapter

object TeamAdapterService {

    private lateinit var teamMenuAdapter: TeamsMenuRecyclerAdapter

    fun getTeamMenuAdapter(): TeamsMenuRecyclerAdapter {
        return teamMenuAdapter
    }

    fun setAdapter(adapter: TeamsMenuRecyclerAdapter) {
        teamMenuAdapter = adapter
    }
}