package com.example.colorimagemobile.ui.home.fragments.teams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.TeamsMenuRecyclerAdapter
import com.example.colorimagemobile.bottomsheets.NewTeamBottomSheet
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.repositories.TeamRepository
import com.example.colorimagemobile.services.teams.TeamAdapterService
import com.example.colorimagemobile.services.teams.TeamService
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.Constants

class TeamsMenuFragment : Fragment(R.layout.fragment_teams_menu) {

    private lateinit var myView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view
        MyFragmentManager(requireActivity()).hideBackButton()
        getAllTeams()
        setListeners()
    }

    private fun setListeners() {
        myView.findViewById<Button>(R.id.newTeamBtn).setOnClickListener {
            val newTeamBS = NewTeamBottomSheet()
            newTeamBS.show(parentFragmentManager, "NewTeamBottomSheet")
        }
    }

    private fun getAllTeams() {
        myView.findViewById<TextView>(R.id.loadingTeamsText).visibility = View.VISIBLE

        TeamRepository().getAllTeams().observe(viewLifecycleOwner, { it ->
            // some error occurred during HTTP request
            if (it.isError as Boolean) {
                CommonFun.printToast(requireContext(), it.message!!)
                return@observe
            }

            myView.findViewById<TextView>(R.id.loadingTeamsText).visibility = View.GONE

            val teams = it.data as ArrayList<TeamModel>
            TeamService.setAllTeams(teams)

            val recyclerView = myView.findViewById<RecyclerView>(R.id.teamsMenuRecyclerView)
            recyclerView.layoutManager = GridLayoutManager(requireContext(), Constants.NB_DATA_ROWS)

            val adapter = TeamsMenuRecyclerAdapter(
                { pos -> TeamService.joinTeam(pos, requireContext())},
                { pos -> openTeam(pos)},
                { pos -> TeamService.leaveTeam(pos, requireContext())})
            recyclerView.adapter = adapter
            TeamAdapterService.setAdapter(adapter)
        })
    }

    private fun openTeam(position: Int) {
        MyFragmentManager(requireActivity()).openWithData(R.id.teamsMenuFrameLayout, TeamsProfileFragment(), Constants.TEAMS.CURRENT_TEAM_ID_KEY, position)
    }
}