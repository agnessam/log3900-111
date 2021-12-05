package com.example.colorimagemobile.services.museum

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import com.example.colorimagemobile.R
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

    fun getPostLikes(position: Int): Int {
        return posts[position].likes.size
    }

    fun likePost(position: Int) {
        posts[position].likes.add(UserService.getUserInfo()._id)
    }

    fun unlikePost(position: Int) {
        posts[position].likes.remove(UserService.getUserInfo()._id)
    }

    fun hasLiked(currentPost: MuseumPostModel): Boolean {
        return currentPost.likes.contains(UserService.getUserInfo()._id)
    }

    fun addCommentToPost(position: Int, commentInterface: CommentInterface) {
        posts[position].comments.add(commentInterface)
    }

    fun createComment(postId: String, comment: String): CommentInterface {
        return CommentInterface(
            content = comment,
            author = UserService.getUserInfo(),
            postId = postId,
            createdAt = null,
            updatedAt = null
        )
    }

    fun createPostDialog(context: Context, resources: Resources): Dialog {
        val dialog = Dialog(context)

        dialog.setContentView(R.layout.modal_post)
        val height = (resources.displayMetrics.heightPixels * 0.70).toInt()
        val width = (resources.displayMetrics.widthPixels * 0.70).toInt()

        dialog.window?.setBackgroundDrawableResource(R.drawable.modal_background)
        dialog.window?.setLayout(width, height)

        return dialog
    }
}