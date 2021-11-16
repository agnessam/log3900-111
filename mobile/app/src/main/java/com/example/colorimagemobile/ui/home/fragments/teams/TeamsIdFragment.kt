package com.example.colorimagemobile.ui.home.fragments.teams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.colorimagemobile.R
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants

class TeamsIdFragment : Fragment(R.layout.fragment_teams_id) {
    private var teamID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            teamID = it.getString(Constants.TEAMS.CURRENT_TEAM_ID_KEY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        printMsg(teamID.toString())
    }
}