package com.example.colorimagemobile.adapter

//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.fragment.app.FragmentActivity
//import androidx.recyclerview.widget.RecyclerView
//import com.example.colorimagemobile.R
//import com.example.colorimagemobile.models.DrawingModel
//import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
//import com.example.colorimagemobile.services.drawing.DrawingService
//import com.example.colorimagemobile.services.socket.DrawingSocketService
//import com.example.colorimagemobile.services.users.UserService
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//
//class CollaborationAdapter (val activity: FragmentActivity,
//drawings: ArrayList<DrawingMenuData>,
//val destination: Int,
//val updateDrawing: (newDrawingInfo: DrawingModel.UpdateDrawing, pos: Int) -> Unit
//): RecyclerView.Adapter<CollaborationAdapter.ViewHolder>() {
//
//    val drawingHistoryOfCollab: ArrayList<DrawingMenuData> = drawings
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollaborationAdapter.ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_collaboration_history, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: CollaborationAdapter.ViewHolder, position: Int) {
//        holder.drawingId.text = DrawingService.getCollaborationDrawingObject()[position].name
//        holder.collaborationHistory.text = UserService.getCollaborationToShow()[position].collaboratedAt.toString()
//    }
//
//    override fun getItemCount(): Int { return drawingHistoryOfCollab.size }
//
//    private fun openDrawing(position: Int, context: Context) {
//        // Set current room to drawing id
//        DrawingService.setCurrentDrawingID(drawingHistoryOfCollab[position].drawing._id)
//
//        // fetch drawing svgString from database
//        runBlocking{
//            val job = launch{
//                DrawingSocketService.joinCurrentDrawingRoom()
//            }
//            job.join()
//            DrawingSocketService.sendGetUpdateDrawingRequest(drawingHistoryOfCollab, position, destination)
//        }
//    }
//
//    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        var drawingId : TextView= itemView.findViewById(R.id.drawingId)
//        var collaborationHistory : TextView= itemView.findViewById(R.id.dateOfCollaboration)
//        init {
//
//            // click listener for clicking on specific drawing
//            itemView.setOnClickListener {
//                val position: Int = bindingAdapterPosition
//                DrawingService.setCurrentDrawingID(DrawingService.getCollaborationDrawingObject()[position]._id)
//                openDrawing(bindingAdapterPosition, itemView.context)
//            }
//
//        }
//    }
//


//}