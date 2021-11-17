package com.example.colorimagemobile.ui.home.fragments.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.SearchModel
import com.example.colorimagemobile.services.teams.TeamAdapterService
import com.example.colorimagemobile.services.teams.TeamService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.tabs.TabLayout

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var queryObject: SearchModel
    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            queryObject = it.getSerializable(Constants.SEARCH.CURRENT_QUERY) as SearchModel
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        recyclerView = myView.findViewById(R.id.searchRecycler)

        setListeners()
    }

    private fun setListeners() {
        myView.findViewById<TabLayout>(R.id.searchTabLayout).addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> setDrawings()
                    1 -> setUsers()
                    else -> setTeams()
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) { }
            override fun onTabUnselected(tab: TabLayout.Tab?) { }
        })
    }

    // add DrawingRecyclerAdapter
    private fun setDrawings() {
        printMsg("Drawings")
        recyclerView.adapter = null
    }

    private fun setUsers() {
        recyclerView.adapter = null
    }

    private fun setTeams() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        TeamService.setAllTeams(queryObject.teams)

        val adapter = TeamAdapterService.createAdapter(requireContext(), requireActivity(), R.layout.recycler_search_team_menu, R.id.searchMainFragment)
        recyclerView.adapter = adapter
        TeamAdapterService.setAdapter(adapter)
    }
}