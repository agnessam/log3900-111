package com.example.colorimagemobile.ui.home.fragments.museum

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.MuseumPostRecyclerAdapter
import com.example.colorimagemobile.models.MuseumPostModel
import com.example.colorimagemobile.repositories.MuseumRepository
import com.example.colorimagemobile.services.museum.MuseumAdapters
import com.example.colorimagemobile.services.museum.MuseumPostService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.closeKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast

class MuseumFragment : Fragment(R.layout.fragment_museum) {

    private lateinit var myView: View
    private lateinit var posts: ArrayList<MuseumPostModel>
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view
        recyclerView = myView.findViewById<RecyclerView>(R.id.museumPostsRecyclerView)

        getAllPosts()
    }

    private fun getAllPosts() {
        MuseumRepository().getAllPosts().observe(viewLifecycleOwner, { it ->
            if (it.isError as Boolean) { return@observe }

            posts = it.data as ArrayList<MuseumPostModel>
            MuseumPostService.setPosts(posts)

            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            val adapter = MuseumPostRecyclerAdapter(
                requireContext(),
                { pos, comment -> postComment(pos, comment)},
                { pos -> likePost(pos) },
                { pos -> unlikePost(pos) })
            recyclerView.adapter = adapter
            MuseumAdapters.addPostsAdapter(adapter)

            val snapHelper: SnapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(recyclerView)
        })
    }

    private fun postComment(position: Int, newComment: String) {
        closeKeyboard(requireActivity())

        if (newComment.isEmpty()) {
            printToast(requireContext(), "Please enter a valid comment!")
            return
        }

        val postId = posts[position]._id
        val comment = MuseumPostService.createComment(postId, newComment)

        MuseumRepository().postComment(postId, comment).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) { return@observe }

            comment.createdAt = it.data?.createdAt
            MuseumPostService.addCommentToPost(postId, comment)
            MuseumAdapters.refreshCommentAdapter(position)
        })
    }

    private fun likePost(position: Int) {
        val postId = posts[position]._id

        MuseumRepository().likePost(postId).observe(viewLifecycleOwner, { it ->
            if (it.isError as Boolean) {
                printToast(requireContext(), it.message!!)
                return@observe
            }

            printMsg(it.data.toString())
            recyclerView.adapter?.notifyItemChanged(position)
        })
    }

    private fun unlikePost(position: Int) {
        val postId = posts[position]._id

        printMsg((postId.toString()))

        MuseumRepository().unlikePost(postId).observe(viewLifecycleOwner, { it ->
            if (it.isError as Boolean) {
                printToast(requireContext(), it.message!!)
                return@observe
            }

            printMsg(it.data.toString())
            recyclerView.adapter?.notifyItemChanged(position)
        })
    }
}