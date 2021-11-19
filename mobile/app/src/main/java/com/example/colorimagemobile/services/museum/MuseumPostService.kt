package com.example.colorimagemobile.services.museum

import com.example.colorimagemobile.models.CommentInterface
import com.example.colorimagemobile.models.MuseumPostModel
import com.example.colorimagemobile.services.users.UserService

object MuseumPostService {

    private lateinit var posts: ArrayList<MuseumPostModel>

    fun setPosts(newPostModel: ArrayList<MuseumPostModel>) {
        posts = newPostModel
    }

    fun getPosts(): ArrayList<MuseumPostModel> {
        return posts
    }

    fun createComment(postId: String, comment: String): CommentInterface {
        return CommentInterface(
            content = comment,
            authorId = UserService.getUserInfo()._id,
            postId = postId,
            createdAt = null,
            updatedAt = null
        )
    }
}