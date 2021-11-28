package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.models.PublishedMuseumPostModel

class PostsMenuRecyclerAdapter(
    val openPost: (String) -> Unit,
    val posts: List<PublishedMuseumPostModel>,
): RecyclerView.Adapter<PostsMenuRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsMenuRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_post_menu, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostsMenuRecyclerAdapter.ViewHolder, position: Int) {
        holder.postName.text = posts[position].name
        holder.nbLikes.text = posts[position].likes.size.toString()
        holder.nbComments.text = posts[position].comments.size.toString()

        val bitmap = ImageConvertor(holder.itemView.context).renderBase64ToBitmap(posts[position].dataUri)
        holder.postImage.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int { return posts.size }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val postName = itemView.findViewById<TextView>(R.id.card_post_menu_name)
        val postImage = itemView.findViewById<ImageView>(R.id.card_post_menu_image)
        val nbLikes = itemView.findViewById<TextView>(R.id.card_post_menu_like_nb)
        val nbComments = itemView.findViewById<TextView>(R.id.card_post_menu_comment_nb)

        init {

            itemView.setOnClickListener {
                openPost(posts[bindingAdapterPosition]._id)
            }
        }
    }
}