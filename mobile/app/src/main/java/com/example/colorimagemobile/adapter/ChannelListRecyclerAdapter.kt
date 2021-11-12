package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

class ChannelListRecyclerAdapter: RecyclerView.Adapter<ChannelListRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelListRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_chat_channels, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelListRecyclerAdapter.ViewHolder, position: Int) {
        val allChannels = TextChannelService.getChannels()
        holder.chanelName.text = allChannels[position].name
    }

    override fun getItemCount(): Int {
       return TextChannelService.getChannels().size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
         var chanelName : TextView = itemView.findViewById<TextView>(R.id.channel_name);

        init {
            itemView.setOnClickListener {
                val position: Int = bindingAdapterPosition

                TextChannelService.setCurrentChannelByPosition(position)
                printMsg(TextChannelService.getCurrentChannel().name)
//                MyFragmentManager(itemView.context as FragmentActivity).open(R.id.main_gallery_fragment, GalleryDrawingFragment())
            }
        }
    }
}