package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.AvatarModel
import com.example.colorimagemobile.services.avatar.AvatarService
import com.google.android.material.imageview.ShapeableImageView

class AvatarRecyclerAdapter:
    RecyclerView.Adapter<AvatarRecyclerAdapter.ViewHolder>() {

    private lateinit var avatars: ArrayList<AvatarModel.AllInfo>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AvatarRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_default_avatar, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvatarRecyclerAdapter.ViewHolder, position: Int) {
        avatars = AvatarService.getAvatars()
        var currentItem = avatars[position]
        holder.avatarUrl = avatars[position].imageUrl
    }

    override fun getItemCount(): Int {
        return AvatarService.getAvatars().size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var avatar : ShapeableImageView
        var avatarUrl : String

        init {
            avatar = itemView.findViewById(R.id.card_avatar)
            avatarUrl = itemView.findViewById<ImageView>(R.id.avatar_url).toString()

        }
    }
}