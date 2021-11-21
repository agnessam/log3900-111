package com.example.colorimagemobile.services.museum

import com.example.colorimagemobile.adapter.MuseumPostRecyclerAdapter
import com.example.colorimagemobile.adapter.PostCommentsRecyclerAdapter

object MuseumAdapters {
    private var postsAdapter: ArrayList<MuseumPostRecyclerAdapter> = arrayListOf()
    private val commentsAdapter: ArrayList<PostCommentsRecyclerAdapter> = arrayListOf()

    fun addPostsAdapter(newPostRecyclerAdapter: MuseumPostRecyclerAdapter) {
        postsAdapter.add(newPostRecyclerAdapter)
    }

    fun addCommentRecycler(commentsRecyclerAdapter: PostCommentsRecyclerAdapter) {
        commentsAdapter.add(commentsRecyclerAdapter)
    }

    fun refreshCommentAdapter(position: Int) {
        commentsAdapter[position].notifyDataSetChanged()
    }
}