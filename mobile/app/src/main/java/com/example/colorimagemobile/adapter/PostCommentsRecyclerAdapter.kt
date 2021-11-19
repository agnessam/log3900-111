package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.MuseumPostModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PostCommentsRecyclerAdapter(currentPost: MuseumPostModel): RecyclerView.Adapter<PostCommentsRecyclerAdapter.ViewHolder>() {

    private val currentPost = currentPost

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentsRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_museum_post_comments, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: PostCommentsRecyclerAdapter.ViewHolder, position: Int) {
        holder.authorName.text = currentPost.comments[position].authorId
        holder.content.text = currentPost.comments[position].content
        holder.date.text = getDate(currentPost.comments[position].createdAt!!)
    }

    private fun getDate(commentDate: String): String {
        val outputFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US)

        val date: Date = inputFormat.parse(commentDate)!!
        return outputFormat.format(date)
    }

    override fun getItemCount(): Int { return currentPost.comments.size }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var authorName : TextView = itemView.findViewById(R.id.post_comment_author);
        var content : TextView = itemView.findViewById(R.id.post_comment_content);
        var date : TextView = itemView.findViewById(R.id.post_comment_date);

        init {
        }
    }
}