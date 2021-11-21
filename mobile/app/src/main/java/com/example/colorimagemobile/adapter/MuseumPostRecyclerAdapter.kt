package com.example.colorimagemobile.adapter

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.services.museum.MuseumAdapters
import com.example.colorimagemobile.services.museum.MuseumPostService

class MuseumPostRecyclerAdapter(
    context: Context,
    val postComment: (Int, String) -> Unit,
    val likePost: (Int) -> Unit,
    val unlikePost: (Int) -> Unit
) : RecyclerView.Adapter<MuseumPostRecyclerAdapter.ViewHolder>() {

    private val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuseumPostRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_museum_posts, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MuseumPostRecyclerAdapter.ViewHolder, position: Int) {
        val currentPost = MuseumPostService.getPosts()[position]
        val bitmap = ImageConvertor(context).base64ToBitmap(currentPost.dataUri)

        if (bitmap != null) {
            holder.image.setImageBitmap(bitmap)
        }

        // set up comments section on the right
        holder.commentRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = PostCommentsRecyclerAdapter(currentPost)
        holder.commentRecyclerView.adapter = adapter
        MuseumAdapters.addCommentRecycler(adapter) // each each comment section in a list

        // set like heart
        if (MuseumPostService.hasLiked(currentPost)) showFilledLike(holder) else hideFilledLike(holder)
    }

    private fun showFilledLike(holder: ViewHolder) {
        holder.unlikeBtn.visibility = View.GONE
        holder.likeBtn.visibility = View.VISIBLE
    }

    private fun hideFilledLike(holder: ViewHolder) {
        holder.unlikeBtn.visibility = View.VISIBLE
        holder.likeBtn.visibility = View.GONE
    }

    override fun getItemCount(): Int { return MuseumPostService.getPosts().size }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.postImage)
        val commentRecyclerView: RecyclerView = itemView.findViewById(R.id.museum_comments_recycler_view)
        private val commentEditText: EditText = itemView.findViewById(R.id.post_comment_input)
        private val postCommentButton: Button = itemView.findViewById(R.id.post_comment_btn)

        val unlikeBtn: ImageButton = itemView.findViewById(R.id.museum_post_like_outline)
        val likeBtn: ImageButton = itemView.findViewById(R.id.museum_post_like_filled)

        init {
            postCommentButton.setOnClickListener {
                postComment(bindingAdapterPosition, commentEditText.text.toString())
                commentEditText.text = null
            }

            // wants to like
            unlikeBtn.setOnClickListener { likePost(bindingAdapterPosition) }

            // wants to unlike
            likeBtn.setOnClickListener { unlikePost(bindingAdapterPosition) }
        }
    }
}