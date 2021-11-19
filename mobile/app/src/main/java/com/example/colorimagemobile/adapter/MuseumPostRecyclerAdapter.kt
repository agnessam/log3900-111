package com.example.colorimagemobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.services.museum.MuseumPostService

class MuseumPostRecyclerAdapter(context: Context) : RecyclerView.Adapter<MuseumPostRecyclerAdapter.ViewHolder>() {

    private val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuseumPostRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_museum_posts, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MuseumPostRecyclerAdapter.ViewHolder, position: Int) {
        val bitmap = ImageConvertor(context).base64ToBitmap(MuseumPostService.getPosts()[position].dataUri)

        if (bitmap != null) {
            holder.image.setImageBitmap(bitmap)
        }
    }

    override fun getItemCount(): Int { return MuseumPostService.getPosts().size }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var image : ImageView = itemView.findViewById(R.id.postImage);

        init {
//            itemView.setOnClickListener { openUser(bindingAdapterPosition) }
        }
    }
}