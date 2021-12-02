package com.example.colorimagemobile.ui.home.fragments.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.DrawingMenuRecyclerAdapter
import com.example.colorimagemobile.adapter.MuseumPostRecyclerAdapter
import com.example.colorimagemobile.adapter.PostsMenuRecyclerAdapter
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.classes.MyPicasso
import com.example.colorimagemobile.classes.NotificationSound.Notification
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.MuseumPostModel
import com.example.colorimagemobile.models.PublishedMuseumPostModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.repositories.MuseumRepository
import com.example.colorimagemobile.repositories.DrawingRepository
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.services.museum.MuseumAdapters
import com.example.colorimagemobile.services.museum.MuseumPostService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.hideKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_teams_profile.*

class UsersProfileFragment : Fragment(R.layout.fragment_users_profile) {
    private var userId: String? = null
    private lateinit var currentUser: UserModel.AllInfoWithData
    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private var drawingsMenu: ArrayList<DrawingMenuData> = arrayListOf()
    private var publishedDrawings: List<PublishedMuseumPostModel> = listOf()
    private lateinit var followBtn: Button
    private lateinit var unfollewBtn: Button
    private lateinit var descriptionCardView : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
         printMsg("value of id in users profile "+it.getString(Constants.USERS.CURRENT_USER_ID_KEY))
            if(it.getString(Constants.USERS.CURRENT_USER_ID_KEY)== null){
                userId = UserService.getUserInfo()._id
            }
            else{
                userId = it.getString(Constants.USERS.CURRENT_USER_ID_KEY)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        recyclerView = myView.findViewById(R.id.userProfileDrawingsRecycler)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), Constants.NB_DATA_ROWS)

        getCurrentUser()
    }

    private fun getCurrentUser() {
        UserRepository().getUserById(UserService.getToken(), userId!!).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) {
                printToast(requireContext(), it.message!!)
                return@observe
            }

            currentUser = it.data!!
            publishedDrawings = it.data.posts

            val drawings = it.data.drawings
            DrawingService.setAllDrawings(drawings)
            drawingsMenu = DrawingService.getDrawingsBitmap(requireContext(), drawings)
            setAllDrawings()

            UserService.setCurrentNbFollowers(currentUser.followers.size)

            updateUI()
            setListeners()
        })
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
        if (UserService.getUserInfo()._id == userId) {
            followBtn.visibility = View.GONE
            unfollewBtn.visibility = View.GONE
        } else {
            updateButtons()
        }
    }

    private fun updateButtons() {
        if (currentUser.followers.contains(UserService.getUserInfo()._id)) {
            hideFollowBtn()
        } else {
            showFollowBtn()
        }
    }

    private fun hideShowDescription(){
        if (currentUser.description.isNullOrBlank()){
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
            UserService.followUser(userId!!, requireContext())
            myView.findViewById<TextView>(R.id.userIdNbOfFollowers).text = UserService.getCurrentNbFollower().toString()
            hideFollowBtn()


        }
        unfollewBtn.setOnClickListener {
            UserService.unfollowUser(userId!!, requireContext())
            showFollowBtn()

            myView.findViewById<TextView>(R.id.userIdNbOfFollowers).text = UserService.getCurrentNbFollower().toString()
        }
    }

    private fun setAllDrawings() {
        recyclerView.adapter = null
        recyclerView.adapter = DrawingMenuRecyclerAdapter(requireActivity(), drawingsMenu, R.id.usersMenuFrameLayout) { updatedDrawing, pos -> updateDrawing(updatedDrawing, pos) }
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
        hideKeyboard(requireContext(), myView)

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