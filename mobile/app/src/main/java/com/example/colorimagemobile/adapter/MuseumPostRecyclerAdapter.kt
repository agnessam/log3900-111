package com.example.colorimagemobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.services.museum.MuseumPostService

class MuseumPostRecyclerAdapter(context: Context, val postComment: (Int, String) -> Unit) : RecyclerView.Adapter<MuseumPostRecyclerAdapter.ViewHolder>() {

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
        holder.commentRecyclerView.adapter = PostCommentsRecyclerAdapter(currentPost)
    }

    override fun getItemCount(): Int { return MuseumPostService.getPosts().size }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.postImage)
        val commentRecyclerView: RecyclerView = itemView.findViewById(R.id.museum_comments_recycler_view)
        private val commentEditText: EditText = itemView.findViewById(R.id.post_comment_input)
        private val postCommentButton: Button = itemView.findViewById(R.id.post_comment_btn)

        init {
            postCommentButton.setOnClickListener {
                postComment(bindingAdapterPosition, commentEditText.text.toString())
//                commentEditText.text = null
            }
        }
    }
}