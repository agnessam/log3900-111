package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.chat.TextChannelService


class ChannelListRecyclerAdapter : RecyclerView.Adapter<ChannelListRecyclerAdapter.ViewHolder>() {

    private var channelName : MutableList<String> = mutableListOf()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChannelListRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_all_chat_channel_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelListRecyclerAdapter.ViewHolder, position: Int) {
        channelName =TextChannelService.getAllTextChannelName()
        holder.chName.text = channelName[position]
    }

    override fun getItemCount(): Int {
       return TextChannelService.getAllTextChannelName().size
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
         var chName : TextView;

        init {
            chName = itemView.findViewById<TextView>(R.id.channel_name)

        }
    }







}