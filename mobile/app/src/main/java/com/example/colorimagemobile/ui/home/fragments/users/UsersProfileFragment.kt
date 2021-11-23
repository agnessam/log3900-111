package com.example.colorimagemobile.ui.home.fragments.users

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.DrawingMenuRecyclerAdapter
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.loadUrl
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_teams_profile.*

class UsersProfileFragment : Fragment(R.layout.fragment_users_profile) {
    private var userPosition: Int? = null
    private lateinit var currentUser: UserModel.AllInfo
    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private val drawingsMenu: ArrayList<DrawingMenuData> = arrayListOf()
    private lateinit var followBtn: Button
    private lateinit var unfollewBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userPosition = it.getInt(Constants.USERS.CURRENT_USER_ID_KEY)
            currentUser = UserService.getUser(userPosition!!)
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
        var userIdImageView : ImageView = myView.findViewById(R.id.userIdImageView);
        myView.findViewById<TextView>(R.id.userIdNameCard).text = currentUser.username
        loadUrl(currentUser.avatar.imageUrl,userIdImageView)
        myView.findViewById<TextView>(R.id.userIdDescription).text = currentUser.description

        followBtn = myView.findViewById<Button>(R.id.FollowBtn)
        unfollewBtn = myView.findViewById<Button>(R.id.UnfollowBtn)
        updateUserButtons()

    }
    private fun updateUserButtons() {
     // to be implement on branch Follow unfollow
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
        myView.findViewById<TabLayout>(R.id.userProfileDrawingsTabLayout).addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

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
            // to be implement on branch follow unfollow
            hideFollowBtn()
        }
        unfollewBtn.setOnClickListener {
            // to be implement on branch follow unfollow
            showFollowBtn()
        }
    }

    private fun getUserDrawings() {

        UserRepository().getUserDrawings(currentUser._id).observe(viewLifecycleOwner, {
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
        recyclerView.adapter = DrawingMenuRecyclerAdapter(drawingsMenu, R.id.usersMenuFrameLayout)
    }

    private fun setPublishedDrawings() {
        val drawings = arrayListOf<DrawingMenuData>()
        recyclerView.adapter = DrawingMenuRecyclerAdapter(drawings, R.id.usersMenuFrameLayout)
    }

}