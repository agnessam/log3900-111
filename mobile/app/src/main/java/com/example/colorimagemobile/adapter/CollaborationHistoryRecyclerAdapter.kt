package com.example.colorimagemobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.services.socket.DrawingSocketService
import com.example.colorimagemobile.services.users.UserService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CollaborationHistoryRecyclerAdapter(
    val activity: FragmentActivity,
    drawings: ArrayList<DrawingMenuData>,
    val destination: Int,
    val updateDrawing: (newDrawingInfo: DrawingModel.UpdateDrawing, pos: Int) -> Unit
) :
    RecyclerView.Adapter<CollaborationHistoryRecyclerAdapter.ViewHolder>() {

    private var drawings : ArrayList<DrawingMenuData> = drawings

    // create card view and sets its contents format
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollaborationHistoryRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_collaboration_history, parent, false)
        return ViewHolder(view)
    }

    // populate each data to cardview
    override fun onBindViewHolder(holder: CollaborationHistoryRecyclerAdapter.ViewHolder, position: Int) {
        holder.drawingName.text = DrawingService.getCollaborationDrawingObject()[position].name
        holder.dateOfCollaboration.text = UserService.getCollaborationToShow()[position].collaboratedAt.toString()
    }

    // identify how many item we pass to view holder
    override fun getItemCount(): Int {
        return DrawingService.getCollaborationDrawingObject().size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var drawingName : TextView = itemView.findViewById(R.id.drawingName);
        var dateOfCollaboration : TextView = itemView.findViewById(R.id.dateOfCollaboration);

        init {
//            drawings = DrawingService.getCollabHistoryDrawingsBitmap()
            itemView.setOnClickListener {
                openDrawing(bindingAdapterPosition,itemView.context)
            }
        }
    }

    private fun openDrawing(position: Int, context: Context) {
        // Set current room to drawing id
        DrawingService.setCurrentDrawingID(drawings[position].drawing._id)

        // fetch drawing svgString from database
        runBlocking{
            val job = launch{
                DrawingSocketService.joinCurrentDrawingRoom()
            }
            job.join()
            DrawingSocketService.sendGetUpdateDrawingRequest(drawings, position, destination)
        }
    }


}