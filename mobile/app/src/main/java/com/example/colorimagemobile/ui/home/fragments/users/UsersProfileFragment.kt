package com.example.colorimagemobile.ui.home.fragments.users

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.DrawingMenuRecyclerAdapter
import com.example.colorimagemobile.adapter.MuseumPostRecyclerAdapter
import com.example.colorimagemobile.adapter.PostsMenuRecyclerAdapter
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.classes.MyPicasso
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.MuseumPostModel
import com.example.colorimagemobile.models.PublishedMuseumPostModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.repositories.MuseumRepository
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.services.museum.MuseumAdapters
import com.example.colorimagemobile.services.museum.MuseumPostService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_teams_profile.*

class UsersProfileFragment : Fragment(R.layout.fragment_users_profile) {
    private var userPosition: Int? = null
    private lateinit var currentUser: UserModel.AllInfo
    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private var drawingsMenu: ArrayList<DrawingMenuData> = arrayListOf()
    private var publishedDrawings: List<PublishedMuseumPostModel> = listOf()
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
        getUserPosts()
    }

    private fun updateUI() {
        MyFragmentManager(requireActivity()).showBackButton()
        var userIdImageView : ImageView = myView.findViewById(R.id.userIdImageView);
        myView.findViewById<TextView>(R.id.userIdNameCard).text = currentUser.username
        MyPicasso().loadImage(currentUser.avatar.imageUrl,userIdImageView)
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
                printToast(requireContext(), it.message!!)
                return@observe
            }
            val drawings = it.data as List<DrawingModel.Drawing>
            drawingsMenu = DrawingService.getDrawingsBitmap(requireContext(), drawings)
            setAllDrawings()
        })
    }


    private fun getUserPosts() {
        UserRepository().getUserPosts(currentUser._id).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) {
                printToast(requireContext(), it.message!!)
                return@observe
            }
            publishedDrawings = it.data as List<PublishedMuseumPostModel>
        })
    }

    private fun setAllDrawings() {
        recyclerView.adapter = null
        recyclerView.adapter = DrawingMenuRecyclerAdapter(requireActivity(), drawingsMenu, R.id.usersMenuFrameLayout)
    }

    private fun setPublishedDrawings() {
        recyclerView.adapter = null
        recyclerView.adapter = PostsMenuRecyclerAdapter({ openPost(it) }, publishedDrawings)
    }

    private fun createDialog(): Dialog {
        val dialog = Dialog(requireContext())

        dialog.setContentView(R.layout.modal_post)
        val height = (resources.displayMetrics.heightPixels * 0.70).toInt()
        val width = (resources.displayMetrics.widthPixels * 0.70).toInt()

        dialog.window?.setBackgroundDrawableResource(R.drawable.modal_background)
        dialog.window?.setLayout(width, height)

        return dialog
    }

    private fun openPost(postId: String) {
        MuseumRepository().getPostById(postId).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) {
                printToast(requireContext(), it.message!!)
                return@observe
            }

            val post = it.data as MuseumPostModel
            MuseumPostService.setPosts(arrayListOf(post))

            val dialog = createDialog()
            val recyclerView = dialog.findViewById<RecyclerView>(R.id.dialogRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val adapter = MuseumPostRecyclerAdapter(
                requireContext(),
                { pos, comment -> postComment(pos, comment)},
                { pos -> likePost(pos) },
                { pos -> unlikePost(pos) })

            recyclerView.adapter = adapter
            MuseumAdapters.setPostsAdapter(adapter)

            dialog.show()
        })
    }

    private fun postComment(position: Int, newComment: String) {

    }

    private fun likePost(position: Int) {

    }

    private fun unlikePost(position: Int) {

    }

}