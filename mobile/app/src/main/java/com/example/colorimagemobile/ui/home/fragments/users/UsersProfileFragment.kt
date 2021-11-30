package com.example.colorimagemobile.ui.home.fragments.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.DrawingMenuRecyclerAdapter
import com.example.colorimagemobile.bottomsheets.FollowersListBottomSheet
import com.example.colorimagemobile.bottomsheets.FollowingListBottomSheet
import com.example.colorimagemobile.bottomsheets.UpdatePasswordBottomSheet
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.classes.MyPicasso
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.repositories.DrawingRepository
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_users_profile.*

class UsersProfileFragment : Fragment(R.layout.fragment_users_profile) {
    private var userPosition: Int? = null
    private lateinit var currentUser: UserModel.AllInfo
    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private var drawingsMenu: ArrayList<DrawingMenuData> = arrayListOf()
    private lateinit var followBtn: Button
    private lateinit var unfollewBtn: Button
    private lateinit var descriptionCardView : CardView
    private  var nbFollowers : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            if (UserService.getUserPosition() == null){
                userPosition = UserService.getUserMePosition()
                UserService.setUserPosition(0)

            }else if(it.getInt(Constants.USERS.CURRENT_USER_ID_KEY)>=0 ){
                userPosition = it.getInt(Constants.USERS.CURRENT_USER_ID_KEY)
                UserService.setUserPosition(userPosition)

            }

            UserService.setRecyclerDataForFollowers()
            UserService.setRecyclerDataForFollowing()
            currentUser = UserService.getUser(userPosition!!)
            nbFollowers = UserService.getUser(userPosition!!).followers.size
            UserService.setCurrentNbFollowers(nbFollowers)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        recyclerView = myView.findViewById(R.id.userProfileDrawingsRecycler)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), Constants.NB_DATA_ROWS)

        updateUI()
        setListeners()
        getUserDrawings()
    }

    private fun updateUI() {
        MyFragmentManager(requireActivity()).showBackButton()

        val userIdImageView : ImageView = myView.findViewById(R.id.userIdImageView);
        myView.findViewById<TextView>(R.id.userIdNameCard).text = currentUser.username
        MyPicasso().loadImage(currentUser.avatar.imageUrl,userIdImageView)
        myView.findViewById<TextView>(R.id.userIdDescription).text = currentUser.description
        myView.findViewById<TextView>(R.id.userIdNbOfPosts).text = currentUser.posts.size.toString()
        myView.findViewById<TextView>(R.id.userIdNbOfFollowers).text = currentUser.followers.size.toString()
        myView.findViewById<TextView>(R.id.userIdNbOfFollowing).text = currentUser.following.size.toString()

        followBtn = myView.findViewById<Button>(R.id.FollowBtn)
        unfollewBtn = myView.findViewById<Button>(R.id.UnfollowBtn)
        updateUserButtons()

        descriptionCardView = myView.findViewById(R.id.userIdDescriptionCardView)
        hideShowDescription()
    }
    private fun updateUserButtons(){
        if (UserService.isCurrentUser(userPosition!!)) {
            followBtn.visibility = View.GONE
            unfollewBtn.visibility = View.GONE
        } else{
            updateButtons()
        }
    }

    private fun updateButtons() {
        if (UserService.isAlreadyFollower(userPosition!!)) {
            hideFollowBtn()
        } else {
            showFollowBtn()
        }
    }

    private fun hideShowDescription(){
        if (UserService.isDescriptionNullOrBlank(userPosition!!)){
            descriptionCardView.visibility = View.GONE
        } else {
            descriptionCardView.visibility = View.VISIBLE
        }

    }

    private fun hideFollowBtn() {
        followBtn.visibility = View.GONE
        unfollewBtn.visibility = View.VISIBLE
    }

    private fun showFollowBtn() {
        followBtn.visibility = View.VISIBLE
        unfollewBtn.visibility = View.GONE
    }

    private fun setListeners() {
        myView.findViewById<TabLayout>(R.id.userProfileDrawingsTabLayout).
        addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> setAllDrawings()
                    else -> setPublishedDrawings()
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) { }
            override fun onTabUnselected(tab: TabLayout.Tab?) { }
        })
        followBtn.setOnClickListener {
            UserService.followUser(userPosition!!, requireContext())
            myView.findViewById<TextView>(R.id.userIdNbOfFollowers).text = UserService.getCurrentNbFollower().toString()
            hideFollowBtn()


        }
        unfollewBtn.setOnClickListener {
            UserService.unfollowUser(userPosition!!, requireContext())
            showFollowBtn()

            myView.findViewById<TextView>(R.id.userIdNbOfFollowers).text = UserService.getCurrentNbFollower().toString()
        }
        showfollower.setOnClickListener {
            val showFollowers = FollowersListBottomSheet()
            showFollowers.show(parentFragmentManager, "showFollowers")
        }
        showfollowingList.setOnClickListener {
            val showFollowing = FollowingListBottomSheet()
            showFollowing.show(parentFragmentManager, "showFollowing")
        }
    }

    private fun getUserDrawings() {
        UserRepository().getUserDrawings(currentUser._id).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) {
                return@observe
            }
            val drawings = it.data as List<DrawingModel.Drawing>
            drawingsMenu = DrawingService.getDrawingsBitmap(requireContext(), drawings)
            setAllDrawings()
        })
    }

    private fun setAllDrawings() {
        recyclerView.adapter = DrawingMenuRecyclerAdapter(requireActivity(), drawingsMenu, R.id.usersMenuFrameLayout) { updatedDrawing, pos -> updateDrawing(updatedDrawing, pos) }
    }

    private fun setPublishedDrawings() {
        val drawings = arrayListOf<DrawingMenuData>()
        recyclerView.adapter = DrawingMenuRecyclerAdapter(requireActivity(), drawings, R.id.usersMenuFrameLayout, {_,_ -> {}})
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