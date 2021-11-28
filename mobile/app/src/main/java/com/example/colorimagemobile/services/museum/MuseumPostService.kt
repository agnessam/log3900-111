package com.example.colorimagemobile.services.museum

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.NotificationSound.Notification
import com.example.colorimagemobile.models.CommentInterface
import com.example.colorimagemobile.models.MuseumPostModel
import com.example.colorimagemobile.repositories.MuseumRepository
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast

object MuseumPostService {

    private lateinit var posts: ArrayList<MuseumPostModel>

    fun setPosts(newPostModel: ArrayList<MuseumPostModel>) {
        posts = newPostModel
    }

    fun getPosts(): ArrayList<MuseumPostModel> {
        return posts
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

    fun postComment(position: Int, newComment: String, lifecycleOwner: LifecycleOwner, context: Context) {
        if (newComment.isEmpty()) {
            printToast(context, "Please enter a valid comment!")
            return
        }

        val postId = posts[position]._id
        val comment = createComment(postId, newComment)

        MuseumRepository().postComment(postId, comment).observe(lifecycleOwner, {
            if (it.isError as Boolean) { return@observe }

            comment.createdAt = it.data?.createdAt
            addCommentToPost(position, comment)
            MuseumAdapters.refreshCommentAdapter(position)
            Notification().playSound(context)
        })
    }

    fun likePostRequest(position: Int,  lifecycleOwner: LifecycleOwner, context: Context) {
        val postId = posts[position]._id

        MuseumRepository().likePost(postId).observe(lifecycleOwner, {
            if (it.isError as Boolean) {
                printToast(context, it.message!!)
                return@observe
            }

            likePost(position)
            MuseumAdapters.refreshLikeSection(position)
        })
    }

    fun unlikePostRequest(position: Int, lifecycleOwner: LifecycleOwner, context: Context) {
        val postId = posts[position]._id

        MuseumRepository().unlikePost(postId).observe(lifecycleOwner, { it ->
            if (it.isError as Boolean) {
                printToast(context, it.message!!)
                return@observe
            }

            unlikePost(position)
            MuseumAdapters.refreshUnlikeSection(position)
        })
    }
}