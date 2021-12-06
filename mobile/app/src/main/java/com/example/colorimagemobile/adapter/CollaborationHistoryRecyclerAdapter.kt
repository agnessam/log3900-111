package com.example.colorimagemobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.bottomsheets.PasswordConfirmationBottomSheet
import com.example.colorimagemobile.classes.DateFormatter
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.PrivacyLevel
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

    // populate each data to cardView
    override fun onBindViewHolder(holder: CollaborationHistoryRecyclerAdapter.ViewHolder, position: Int) {
        holder.drawingName.text = drawings[position].drawing.name
        holder.dateOfCollaboration.text = DateFormatter.getDate(UserService.getCollaborationToShow()[position].collaboratedAt.toString())
    }

    // identify how many item we pass to view holder
    override fun getItemCount(): Int { return drawings.size }

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


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var drawingName : TextView = itemView.findViewById(R.id.drawingName);
        var dateOfCollaboration : TextView = itemView.findViewById(R.id.dateOfCollaboration);

        init {

            itemView.setOnClickListener {
                val position: Int = bindingAdapterPosition

                if (drawings[position].drawing.privacyLevel == PrivacyLevel.PROTECTED.toString()) {
                    // show password dialog
                    val title = "You're about to join ${drawings[position].drawing.name}'s collaborative session."
                    val description = "The owner has set the privacy level of this drawing to protected. \n" +
                            "Enter the correct password to gain access to this session."
                    val passwordConfirmation = PasswordConfirmationBottomSheet(
                        activity,
                        drawings[position].drawing.password,
                        title,
                        description,
                        "Join",
                        "Enter the session's password"
                    ) { openDrawing(bindingAdapterPosition, itemView.context) }
                    passwordConfirmation.show(activity.supportFragmentManager, "PasswordConfirmationBottomSheet")

                    return@setOnClickListener
                }
                openDrawing(bindingAdapterPosition,itemView.context)
            }

        }
    }



}