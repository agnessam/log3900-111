package com.example.colorimagemobile.services.museum

import com.example.colorimagemobile.adapter.MuseumPostRecyclerAdapter
import com.example.colorimagemobile.adapter.PostCommentsRecyclerAdapter

object MuseumAdapters {
    private lateinit var postsAdapter: MuseumPostRecyclerAdapter
    private val commentsAdapter: HashMap<Int, PostCommentsRecyclerAdapter> = hashMapOf()

    fun setPostsAdapter(newPostRecyclerAdapter: MuseumPostRecyclerAdapter) {
        postsAdapter = newPostRecyclerAdapter
    }

    fun setCommentRecycler(position: Int, commentsRecyclerAdapter: PostCommentsRecyclerAdapter) {
        commentsAdapter[position] = commentsRecyclerAdapter
    }

    fun refreshPostAdapter(position: Int) {
        postsAdapter.notifyItemChanged(position)
    }

    fun getCommentAdapter(position: Int): PostCommentsRecyclerAdapter {
        return commentsAdapter[position]!!
    }

    fun refreshCommentAdapter(position: Int) {
        commentsAdapter[position]?.notifyDataSetChanged()
    }
}