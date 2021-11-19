package com.example.colorimagemobile.services.museum

import com.example.colorimagemobile.models.MuseumPostModel

object MuseumPostService {

    private lateinit var posts: ArrayList<MuseumPostModel>

    fun setPosts(newPostModel: ArrayList<MuseumPostModel>) {
        posts = newPostModel
    }

    fun getPosts(): ArrayList<MuseumPostModel> {
        return posts
    }
}