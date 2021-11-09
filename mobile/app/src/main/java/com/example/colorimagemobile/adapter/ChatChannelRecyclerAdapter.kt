package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg


class ChatChannelRecyclerAdapter : RecyclerView.Adapter<ChatChannelRecyclerAdapter.ViewHolder>() {

    private var channelName = arrayOf("bobo","boba","bobim","bobo","bobo")


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatChannelRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_all_chat_channel_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatChannelRecyclerAdapter.ViewHolder, position: Int) {
       holder.chName.text = channelName[position]
    }

    override fun getItemCount(): Int {
        printMsg("size= "+channelName.size)
       return channelName.size
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
         var chName : TextView;

        init {
            chName = itemView.findViewById<TextView>(R.id.channel_name)
        }
    }
}