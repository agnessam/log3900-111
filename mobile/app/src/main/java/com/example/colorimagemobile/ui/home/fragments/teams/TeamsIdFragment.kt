package com.example.colorimagemobile.ui.home.fragments.teams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.repositories.TeamRepository
import com.example.colorimagemobile.services.teams.TeamService
import com.example.colorimagemobile.utils.CommonFun.Companion.toggleButton
import com.example.colorimagemobile.utils.Constants

class TeamsIdFragment : Fragment(R.layout.fragment_teams_id) {
    private var teamPosition: Int? = null
    private lateinit var currentTeam: TeamModel
    private lateinit var myView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            teamPosition = it.getInt(Constants.TEAMS.CURRENT_TEAM_ID_KEY)
            currentTeam = TeamService.getTeam(teamPosition!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view

        updateUI()
        getTeamDrawings()
    }

    private fun updateUI() {
        myView.findViewById<TextView>(R.id.teamIdNameCard).text = currentTeam.name
//        myView.findViewById<ImageView>(R.id.teamIdImageView).
        myView.findViewById<TextView>(R.id.teamIdNbOfMembers).text = "${currentTeam.members.size} members"
        myView.findViewById<TextView>(R.id.teamIdDescription).text = currentTeam.description

        if (TeamService.shouldHideJoinTeamButton(teamPosition!!)) {
            val joinBtn = myView.findViewById<Button>(R.id.teamIdJoinBtn)
            joinBtn.text = TeamService.JOINED_KEYWORD
            toggleButton(joinBtn, false)
        }
    }

    private fun getTeamDrawings() {
        TeamRepository().getTeamDrawings(currentTeam._id).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) {
                return@observe
            }

            val drawings = it.data as ArrayList<DrawingModel.CreateDrawing>
        })
    }
}