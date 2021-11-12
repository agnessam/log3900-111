package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R

class FollowNotificationRecyclerAdapter : RecyclerView.Adapter<FollowNotificationRecyclerAdapter.ViewHolder>() {

    private  var userName : MutableList<String> = mutableListOf()

    override fun onBindViewHolder(holder: FollowNotificationRecyclerAdapter.ViewHolder, position: Int) {

        holder.userName.text = userName[position]
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var userName : TextView;

        init {
            userName = itemView.findViewById<TextView>(R.id.channel_name)

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowNotificationRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_all_chat_channel_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userName.size
    }
}