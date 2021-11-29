package com.example.colorimagemobile.ui.home.fragments.teams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.DrawingMenuRecyclerAdapter
import com.example.colorimagemobile.adapter.MuseumPostRecyclerAdapter
import com.example.colorimagemobile.adapter.PostsMenuRecyclerAdapter
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.classes.NotificationSound.Notification
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.repositories.MuseumRepository
import com.example.colorimagemobile.repositories.DrawingRepository
import com.example.colorimagemobile.repositories.TeamRepository
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.services.museum.MuseumAdapters
import com.example.colorimagemobile.services.museum.MuseumPostService
import com.example.colorimagemobile.services.teams.TeamService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.toggleButton
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.tabs.TabLayout

class TeamsProfileFragment : Fragment(R.layout.fragment_teams_profile) {
    private var teamPosition: Int? = null
    private lateinit var currentTeam: TeamModel
    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private var drawingsMenu: ArrayList<DrawingMenuData> = arrayListOf()
    private var publishedDrawings: List<PublishedMuseumPostModel> = listOf()
    private lateinit var joinBtn: Button
    private lateinit var leaveBtn: Button
    private lateinit var deleteBtn: Button

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
        getCurrentTeam()
    }

    private fun updateUI() {
        MyFragmentManager(requireActivity()).showBackButton()
        
        myView.findViewById<TextView>(R.id.teamIdNameCard).text = currentTeam.name

//        myView.findViewById<ImageView>(R.id.teamIdImageView).
        myView.findViewById<TextView>(R.id.teamIdNbOfMembers).text = "${currentTeam.members.size} members"
        myView.findViewById<TextView>(R.id.teamIdDescription).text = currentTeam.description

        joinBtn = myView.findViewById<Button>(R.id.teamIdJoinBtn)
        leaveBtn = myView.findViewById<Button>(R.id.leaveTeamIdBtn)
        deleteBtn = myView.findViewById<Button>(R.id.deleteTeamIdBtn)

        if (currentTeam.owner == UserService.getUserInfo()._id) {
            myView.findViewById<RelativeLayout>(R.id.teamIdJoinBtnMain).visibility = View.GONE
            myView.findViewById<RelativeLayout>(R.id.deleteTeamIdBtnMain).visibility = View.VISIBLE
        } else {
            myView.findViewById<RelativeLayout>(R.id.teamIdJoinBtnMain).visibility = View.VISIBLE
            myView.findViewById<RelativeLayout>(R.id.deleteTeamIdBtnMain).visibility = View.GONE
            updateTeamButtons()
        }
    }

    private fun updateTeamButtons() {
        if (TeamService.isUserAlreadyTeamMember(teamPosition!!)) {
            hideJoinBtn()
        } else {
            showJoinBtn()
            checkIsTeamFull(joinBtn)
        }
    }

    private fun hideJoinBtn() {
        joinBtn.visibility = View.GONE
        leaveBtn.visibility = View.VISIBLE
    }

    private fun showJoinBtn() {
        joinBtn.visibility = View.VISIBLE
        leaveBtn.visibility = View.GONE
    }

    private fun checkIsTeamFull(button: Button) {
        currentTeam.memberLimit?.let {
            if (currentTeam.members.size >= currentTeam.memberLimit!!) {
                toggleButton(button, false)
            }
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

        joinBtn.setOnClickListener {
            TeamService.joinTeam(teamPosition!!, requireContext())
            hideJoinBtn()
        }
        leaveBtn.setOnClickListener {
            TeamService.leaveTeam(teamPosition!!, requireContext())
            showJoinBtn()
        }
        deleteBtn.setOnClickListener {
            TeamService.deleteTeam(teamPosition!!, requireContext())
        }
    }

    private fun getCurrentTeam() {
        TeamRepository().getTeamById(currentTeam._id).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) {
                printToast(requireContext(), it.message!!)
                return@observe
            }

            val team = it.data as TeamIdModel
            drawingsMenu = DrawingService.getDrawingsBitmap(requireContext(), team.drawings)
            publishedDrawings = team.posts
            setAllDrawings()
        })
    }

    private fun setAllDrawings() {
        recyclerView.adapter = null
        recyclerView.adapter = DrawingMenuRecyclerAdapter(requireActivity(), drawingsMenu, R.id.teamsMenuFrameLayout, {_,_ -> {}})
//        recyclerView.adapter = DrawingMenuRecyclerAdapter(requireActivity(), drawingsMenu, R.id.teamsMenuFrameLayout) { updatedDrawing, pos -> updateDrawing(updatedDrawing, pos) }
    }

    private fun setPublishedDrawings() {
        recyclerView.adapter = null
        recyclerView.adapter = PostsMenuRecyclerAdapter({ openPost(it) }, publishedDrawings)
    }

    private fun openPost(postPosition: Int) {
        MuseumRepository().getPostById(publishedDrawings[postPosition]._id).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) {
                printToast(requireContext(), it.message!!)
                return@observe
            }

            val post = it.data as MuseumPostModel
            MuseumPostService.setPosts(arrayListOf(post))

            val dialog = MuseumPostService.createPostDialog(requireContext(), resources)
            val recyclerView = dialog.findViewById<RecyclerView>(R.id.dialogRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val adapter = MuseumPostRecyclerAdapter(
                requireContext(),
                { _, comment -> postComment(postPosition, comment)},
                { _ -> likePost(postPosition) },
                { _ -> unlikePost(postPosition) })

            recyclerView.adapter = adapter
            MuseumAdapters.setPostsAdapter(adapter)

            dialog.show()
        })
    }

    private fun postComment(postPosition: Int, newComment: String) {
        CommonFun.hideKeyboard(requireContext(), myView)

        if (newComment.isEmpty()) {
            printToast(requireContext(), "Please enter a valid comment!")
            return
        }

        val postId = publishedDrawings[postPosition]._id
        val comment = MuseumPostService.createComment(postId, newComment)

        MuseumRepository().postComment(postId, comment).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) { return@observe }

            comment.createdAt = it.data?.createdAt
            MuseumPostService.addCommentToPost(0, comment)
            publishedDrawings[postPosition].comments.add("New Comment")

            recyclerView.adapter?.notifyItemChanged(postPosition)
            MuseumAdapters.refreshCommentAdapter(0)
            Notification().playSound(requireContext())
        })
    }

    private fun likePost(postPosition: Int) {
        MuseumRepository().likePost(publishedDrawings[postPosition]._id).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) {
                printToast(requireContext(), it.message!!)
                return@observe
            }

            publishedDrawings[postPosition].likes.add(UserService.getUserInfo()._id)
            recyclerView.adapter?.notifyItemChanged(postPosition)
            MuseumAdapters.refreshLikeSection(0)
        })
    }

    private fun unlikePost(postPosition: Int) {
        MuseumRepository().unlikePost(publishedDrawings[postPosition]._id).observe(viewLifecycleOwner, { it ->
            if (it.isError as Boolean) {
                printToast(requireContext(), it.message!!)
                return@observe
            }

            publishedDrawings[postPosition].likes.remove(UserService.getUserInfo()._id)
            recyclerView.adapter?.notifyItemChanged(postPosition)
            MuseumAdapters.refreshUnlikeSection(0)
        })
    }

    private fun updateDrawing(updatedDrawing: DrawingModel.UpdateDrawing, position: Int) {
        DrawingRepository().updateDrawing(drawingsMenu[position].drawing._id!!, updatedDrawing).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) {
                printToast(requireActivity(), it.message!!)
                return@observe
            }

            drawingsMenu[position] = DrawingService.updateDrawingFromMenu(drawingsMenu[position], updatedDrawing)
            recyclerView.adapter?.notifyItemChanged(position)
        })
    }
}