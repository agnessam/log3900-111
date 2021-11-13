package com.example.colorimagemobile.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.ui.home.fragments.chat.ChatMessageBoxFragment

class ChannelListRecyclerAdapter: RecyclerView.Adapter<ChannelListRecyclerAdapter.ViewHolder>() {

    private var currentPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelListRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_chat_channels, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelListRecyclerAdapter.ViewHolder, position: Int) {
        val allChannels = TextChannelService.getChannels()
        holder.chanelName.text = allChannels[position].name

        val backgroundColor = if (position == currentPosition) "#f5f5f5" else "#ffffff"
        holder.chanelName.setBackgroundColor(Color.parseColor(backgroundColor))
    }

    override fun getItemCount(): Int {
       return TextChannelService.getChannels().size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
         var chanelName : TextView = itemView.findViewById<TextView>(R.id.channel_name);

        init {
            itemView.setOnClickListener {
                currentPosition = bindingAdapterPosition

                TextChannelService.setCurrentChannelByPosition(currentPosition)
                MyFragmentManager(itemView.context as FragmentActivity).open(R.id.chat_channel_framelayout, ChatMessageBoxFragment())
                notifyDataSetChanged()
            }
        }
    }
}