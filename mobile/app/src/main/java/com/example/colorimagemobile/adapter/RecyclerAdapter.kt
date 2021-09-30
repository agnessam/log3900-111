package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.model.Message
import kotlin.collections.ArrayList

class RecyclerAdapter(message: ArrayList<Message>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    // our card arrays
    var message: ArrayList<Message>

    init {
        this.message = message
    }

    // creates card view referencing to individual cards
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_own_chat, parent, false)
        return ViewHolder(view)
    }

    // populate our data to card view - iterates over getItemCount() ?
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.author.text = message[position].author
        holder.message.text = message[position].message
        holder.timestamp.text = message[position].timestamp
    }

    // how many items we pass to our view holder
    override fun getItemCount(): Int {
        return message.size
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