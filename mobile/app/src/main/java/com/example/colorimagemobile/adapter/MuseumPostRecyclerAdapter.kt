package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.museum.MuseumPostService

class MuseumPostRecyclerAdapter : RecyclerView.Adapter<MuseumPostRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuseumPostRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_museum_posts, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MuseumPostRecyclerAdapter.ViewHolder, position: Int) {
//        holder.username.text = MuseumPostService.getPosts()[position].name
    }

    override fun getItemCount(): Int { return MuseumPostService.getPosts().size }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
//            itemView.setOnClickListener { openUser(bindingAdapterPosition) }
        }
    }
}