package com.example.colorimagemobile.ui.home.fragments.teams

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.DrawingMenuRecyclerAdapter
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.repositories.TeamRepository
import com.example.colorimagemobile.services.teams.TeamService
import com.example.colorimagemobile.utils.CommonFun.Companion.toggleButton
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.tabs.TabLayout

class TeamsProfileFragment : Fragment(R.layout.fragment_teams_profile) {
    private var teamPosition: Int? = null
    private lateinit var currentTeam: TeamModel
    private lateinit var myView: View
    private val drawingsMenu: ArrayList<DrawingMenuData> = arrayListOf()
    private lateinit var recyclerView: RecyclerView

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
        recyclerView = myView.findViewById(R.id.teamProfileDrawingsRecycler)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), Constants.NB_DATA_ROWS)

        updateUI()
        setListeners()
        getTeamDrawings()
    }

    private fun updateUI() {
        MyFragmentManager(requireActivity()).showBackButton()
        
        myView.findViewById<TextView>(R.id.teamIdNameCard).text = currentTeam.name
//        myView.findViewById<ImageView>(R.id.teamIdImageView).
        myView.findViewById<TextView>(R.id.teamIdNbOfMembers).text = "${currentTeam.members.size} members"
        myView.findViewById<TextView>(R.id.teamIdDescription).text = currentTeam.description

        val joinBtn = myView.findViewById<Button>(R.id.teamIdJoinBtn)
        val leaveBtn = myView.findViewById<Button>(R.id.leaveTeamIdBtn)

        if (TeamService.isUserAlreadyTeamMember(teamPosition!!)) {
            joinBtn.visibility = View.GONE
            leaveBtn.visibility = View.VISIBLE
        } else {
            joinBtn.visibility = View.VISIBLE
            leaveBtn.visibility = View.GONE
        }
    }

    private fun setListeners() {
        myView.findViewById<TabLayout>(R.id.teamProfileDrawingsTabLayout).addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> setAllDrawings()
                    else -> setPublishedDrawings()
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) { }
            override fun onTabUnselected(tab: TabLayout.Tab?) { }
        })
    }

    private fun getTeamDrawings() {
        TeamRepository().getTeamDrawings(currentTeam._id).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) {
                return@observe
            }

            /**
             * CREATE OBJECT TO HANDLE DUPLICATE LOGIC (IN GALLERY MENU FRAGMENT)
             */
            val drawings = it.data as ArrayList<DrawingModel.CreateDrawing>
            drawings.forEach { drawing ->
                val bitmap: Bitmap? = ImageConvertor(requireContext()).base64ToBitmap(drawing.dataUri)

                if (bitmap != null) {
                    drawingsMenu.add(DrawingMenuData(drawing._id!!, bitmap))
                }
            }
            setAllDrawings()
        })
    }

    /**
     * TO CHANGE LOGIC OF OPENING DRAWING
     */
    private fun setAllDrawings() {
        recyclerView.adapter = DrawingMenuRecyclerAdapter(drawingsMenu, R.id.teamsMenuFrameLayout)
    }

    private fun setPublishedDrawings() {
        val drawings = arrayListOf<DrawingMenuData>()
        recyclerView.adapter = DrawingMenuRecyclerAdapter(drawings, R.id.teamsMenuFrameLayout)
    }
}