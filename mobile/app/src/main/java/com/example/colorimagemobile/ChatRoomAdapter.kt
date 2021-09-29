package com.example.colorimagemobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.databinding.MessageReceivedTextBinding
import com.example.colorimagemobile.databinding.MessageSentTextBinding
import com.example.colorimagemobile.ui.MessageWithType


class ChatRoomAdapter(private val layoutInflater: LayoutInflater, private val chatList : ArrayList<MessageWithType>) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val message = itemView.findViewById<TextView>(R.id.messageContent)
        val timestamp = itemView.findViewById<TextView>(R.id.timestamp)
        val author = itemView.findViewById<TextView>(R.id.username)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        when (viewType) {
            MessageType.MESSAGE_SENT.ordinal -> {
                view = MessageSentTextBinding.inflate(layoutInflater).root
            }

            MessageType.MESSAGE_RECEIVED.ordinal -> {
                view = MessageReceivedTextBinding.inflate(layoutInflater).root
            }
        }
        return ViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val messageData  = chatList[position]
        val userName = messageData.author
        val content = messageData.message
        val timestamp = messageData.timestamp

        when(messageData.viewType) {

            MessageType.MESSAGE_SENT.ordinal -> {
                holder.author.setText(userName)
                holder.message.setText(content)
                holder.timestamp.setText(timestamp)
            }
            MessageType.MESSAGE_RECEIVED.ordinal ->{
                holder.author.setText(userName)
                holder.message.setText(content)
                holder.timestamp.setText(timestamp)
            }
        }
    }

    override fun getItemCount(): Int {
        return chatList.size;
    }

    override fun getItemViewType(position: Int): Int {
        return chatList[position].viewType
    }
}