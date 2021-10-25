package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import kotlin.collections.ArrayList
import com.example.colorimagemobile.models.Message


class ChatMessageRecyclerAdapter(message: ArrayList<Message>, currentAuthor: String): RecyclerView.Adapter<ChatMessageRecyclerAdapter.ViewHolder>() {
    // our card arrays
    var message: ArrayList<Message>
    var currentAuthor: String

    private val THEIR_CHAT = 0
    private val OWN_CHAT = 1

    init {
        this.message = message
        this.currentAuthor = currentAuthor
    }

    // creates card view referencing to individual cards
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageRecyclerAdapter.ViewHolder {
        return if (viewType === OWN_CHAT) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_own_chat, parent, false)
            ViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_other_chat, parent, false)
            ViewHolder(view)
        }
    }

    // populate our data to card view - iterates over getItemCount() ?
    override fun onBindViewHolder(holder: ChatMessageRecyclerAdapter.ViewHolder, position: Int) {
        holder.author.text = message[position].author
        holder.message.text = message[position].message
        holder.timestamp.text = message[position].timestamp
    }

    // how many items we pass to our view holder
    override fun getItemCount(): Int {
        return message.size
    }

    // calculate whose chat it is
    override fun getItemViewType(position: Int): Int {
        return if (message.get(position).author.equals(this.currentAuthor)) {
            OWN_CHAT
        } else {
            THEIR_CHAT
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var author: TextView
        var message: TextView
        var timestamp: TextView

        init {
            author = itemView.findViewById(R.id.author)
            message = itemView.findViewById(R.id.message)
            timestamp = itemView.findViewById(R.id.timestamp)
        }
    }
}