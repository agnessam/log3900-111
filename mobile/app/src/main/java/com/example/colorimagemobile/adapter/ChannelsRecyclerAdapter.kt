package com.example.colorimagemobile.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.NotificationSound.Notification
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.services.chat.ChatService
import com.example.colorimagemobile.services.chat.TextChannelService

class ChannelsRecyclerAdapter():
    RecyclerView.Adapter<ChannelsRecyclerAdapter.ViewHolder>() {

    private var isAllChannels = true
    private var currentPosition: Int = -1
    private lateinit var channels: ArrayList<TextChannelModel.AllInfo>
    private var unreadChannels: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelsRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_chat_channels, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelsRecyclerAdapter.ViewHolder, position: Int) {
        holder.chanelName.text = channels[position].name

        val backgroundColor = if (position == currentPosition) "#f5f5f5" else "#ffffff"
        holder.chanelName.setBackgroundColor(Color.parseColor(backgroundColor))
        if (!isAllChannels && unreadChannels.contains(channels[position].name)) {
            holder.chanelName.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.ic_baseline_circle_24, 0)
        } else {
            holder.chanelName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        }
    }

    override fun getItemCount(): Int {
       return channels.size
    }

    fun setData(newChannels: ArrayList<TextChannelModel.AllInfo>) {
        channels = newChannels
        notifyDataSetChanged()
    }

    fun setIsAllChannels(isAll: Boolean) {
        this.isAllChannels = isAll
    }

    fun setUnreadChannels(channels: ArrayList<String>) {
        unreadChannels = channels
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
         var chanelName : TextView = itemView.findViewById<TextView>(R.id.channel_name);

        init {
            itemView.setOnClickListener {
                currentPosition = bindingAdapterPosition
                if (currentPosition != -1) {
                    TextChannelService.setCurrentChannelByPosition(currentPosition, isAllChannels)
                    ChatService.unreadChannels.remove(TextChannelService.getCurrentChannel().name)
                    unreadChannels.remove(TextChannelService.getCurrentChannel().name)
                }
                TextChannelService.refreshChannelList()
                ChatService.refreshChatBox(itemView.context as FragmentActivity)
            }
        }
    }
}