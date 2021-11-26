package com.example.colorimagemobile.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.services.NotificationService
import com.example.colorimagemobile.services.chat.ChatService
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants


class ChannelsRecyclerAdapter():
    RecyclerView.Adapter<ChannelsRecyclerAdapter.ViewHolder>() {

    private var isAllChannels = true
    private var currentPosition: Int = -1
    private lateinit var channels: ArrayList<TextChannelModel.AllInfo>
    private var notification : HashMap<String, Int> = HashMap()
    private var test = 12


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelsRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_chat_channels, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelsRecyclerAdapter.ViewHolder, position: Int) {
        holder.chanelName.text = channels[position].name
        holder.nbPendingMessage.text = test.toString()

        val backgroundColor = if (position == currentPosition) "#f5f5f5" else "#ffffff"
        holder.chanelName.setBackgroundColor(Color.parseColor(backgroundColor))
        printMsg("before check if counter is >0: "+NotificationService.getCounter())

        if (NotificationService.getCounter() > 0){

            printMsg("inside getcounter>0" +NotificationService.getCounter())
            notification = NotificationService.getPendingNotification()
            printMsg("voici la map au complet depuis le recycler adapter ++++++++++++++++ "+notification.entries)
            notification.keys.forEach { key ->
              if (channels[position].name  == key)  {
                  printMsg("set vue visible : ==================")
                  holder.nbPendingMessage.setVisibility(View.VISIBLE)
                  printMsg("channel.name value========================: "+channels[position].name )
                  printMsg("key value: ================================="+key)
//                  val realCompte = notification[key]!!/Constants.LISTEN_MESSAGE_OCCURENCE
//                  notification[key]= realCompte
                  printMsg("notification[key].tostring = "+notification[key].toString())
                  holder.nbPendingMessage.text = notification[key].toString()
                  printMsg("set value nb of message .text : ====================="+holder.nbPendingMessage.text)
//                  NotificationService.addToPendingMessage(notification)
                  holder.nbPendingMessage.setVisibility(View.VISIBLE)
              }
            }
        }
        printMsg("if notification > 0 done")
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

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
         var chanelName : TextView  = itemView.findViewById(R.id.channel_name)
        var nbPendingMessage : TextView = itemView.findViewById(R.id.Hbadge_counter)

        init {
            itemView.setOnClickListener {
                currentPosition = bindingAdapterPosition

                TextChannelService.setCurrentChannelByPosition(currentPosition, isAllChannels)
                TextChannelService.refreshChannelList()
                ChatService.refreshChatBox(itemView.context as FragmentActivity)
            }
        }

    }

}