package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyPicasso
import com.example.colorimagemobile.services.users.UserService

class FollowingListRecyclerAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<FollowingListRecyclerAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: FollowingListRecyclerAdapter.ViewHolder, position: Int) {

        holder.username.text = UserService.getRecyclerDataForFollowingList()[position].username
        MyPicasso().loadImage(UserService.getRecyclerDataForFollowingList()[position].avatar.imageUrl, holder.image)
    }

    override fun getItemCount(): Int {
        return UserService.getRecyclerDataForFollowingList().size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var image : ImageView = itemView.findViewById(R.id.card_user_imageview);
        var username : TextView = itemView.findViewById(R.id.card_user_username);
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position!=RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingListRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_user_menu,parent,false)
        return ViewHolder(view)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}