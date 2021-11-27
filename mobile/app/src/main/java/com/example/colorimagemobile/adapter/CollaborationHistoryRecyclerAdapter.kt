package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.CollaborationHistory
import com.example.colorimagemobile.services.users.UserService

class CollaborationHistoryRecyclerAdapter(private val listener: OnItemClickListener):
    RecyclerView.Adapter<CollaborationHistoryRecyclerAdapter.ViewHolder>() {

    private lateinit var collaborationHistory: ArrayList<CollaborationHistory.drawingHistory>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollaborationHistoryRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_collaboration_history, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollaborationHistoryRecyclerAdapter.ViewHolder, position: Int) {

       collaborationHistory = UserService.getCollaborationToShow()
       holder.drawingId.text = collaborationHistory[position].drawing
       holder.collaborationHistory.text = collaborationHistory[position].collaboratedAt.toString()
    }


    override fun getItemCount(): Int {
        return UserService.getCollaborationToShow().size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        var drawingId : TextView
        var collaborationHistory : TextView

        init {
            drawingId = itemView.findViewById(R.id.drawingId)
            collaborationHistory = itemView.findViewById(R.id.dateOfCollaboration)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position!=RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}