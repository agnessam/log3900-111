package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.users.UserService

class CollaborationHistoryRecyclerAdapter(
    val layoutID: Int,
    val openDrawing: (Int) -> Unit):
    RecyclerView.Adapter<CollaborationHistoryRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollaborationHistoryRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutID, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollaborationHistoryRecyclerAdapter.ViewHolder, position: Int) {

       holder.drawingId.text = UserService.getCollaborationToShow()[position].drawing
       holder.collaborationHistory.text = UserService.getCollaborationToShow()[position].collaboratedAt.toString()
    }

    override fun getItemCount(): Int {
        return UserService.getCollaborationToShow().size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var drawingId : TextView= itemView.findViewById(R.id.drawingId)
        var collaborationHistory : TextView= itemView.findViewById(R.id.dateOfCollaboration)

        init {
            itemView.setOnClickListener { openDrawing(bindingAdapterPosition) }
        }
    }
}