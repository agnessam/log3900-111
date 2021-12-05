package com.example.colorimagemobile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.bottomsheets.ConfirmationBottomSheet
import com.example.colorimagemobile.bottomsheets.EditDrawingBottomSheet
import com.example.colorimagemobile.bottomsheets.PasswordConfirmationBottomSheet
import com.example.colorimagemobile.classes.DateFormatter
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.classes.MyPicasso
import com.example.colorimagemobile.enumerators.ButtonType
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.OwnerModel
import com.example.colorimagemobile.models.PrivacyLevel
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuViewHolder
import com.example.colorimagemobile.services.drawing.DrawingOwnerService
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.services.socket.DrawingSocketService
import com.example.colorimagemobile.services.teams.TeamService
import com.example.colorimagemobile.ui.home.fragments.teams.TeamsProfileFragment
import com.example.colorimagemobile.ui.home.fragments.users.UsersProfileFragment
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList

class DrawingMenuRecyclerAdapter(
    val activity: FragmentActivity,
    drawings: ArrayList<DrawingMenuData>,
    val destination: Int,
    val updateDrawing: (newDrawingInfo: DrawingModel.UpdateDrawing, pos: Int) -> Unit,
    val deleteDrawing: (drawingId: String, pos: Int) -> Unit
): RecyclerView.Adapter<DrawingMenuRecyclerAdapter.ViewHolder>() {

    val drawingMenus: ArrayList<DrawingMenuData> = drawings

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawingMenuRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout. recycler_drawing_menu, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DrawingMenuRecyclerAdapter.ViewHolder, position: Int) {
        holder.drawingMenuViewHolder.name.text = drawingMenus[position].drawing.name
        holder.drawingMenuViewHolder.authorName.text = DrawingOwnerService.getUsername(drawingMenus[position].drawing.owner)
        holder.drawingMenuViewHolder.drawingDate.text = DateFormatter.getDate(drawingMenus[position].drawing.createdAt!!)
        holder.drawingMenuViewHolder.image.setImageBitmap(drawingMenus[position].imageBitmap)

        val avatar = DrawingOwnerService.getAvatar(drawingMenus[position].drawing.owner)
        if (avatar != null) {
            MyPicasso().loadImage(avatar.imageUrl, holder.drawingMenuViewHolder.authorImageView)
        }

        if (drawingMenus[position].drawing.privacyLevel == PrivacyLevel.PROTECTED.toString())
            holder.drawingMenuViewHolder.lockIconView.visibility = View.VISIBLE

        holder.drawingMenuViewHolder.privacyLevel.text = drawingMenus[position].drawing.privacyLevel.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }

        val nbCollaborators = drawingMenus[position].drawing.collaborators!!.size
        holder.drawingMenuViewHolder.collaborators.text = "${nbCollaborators} collaborator${if (nbCollaborators > 1) "s" else ""}"

        if (!DrawingService.isOwner(drawingMenus[position].drawing)) {
            holder.drawingMenuViewHolder.popupMenu.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int { return drawingMenus.size }

    private fun openDrawing(position: Int, context: Context) {
        // Set current room to drawing id
        DrawingService.setCurrentDrawingID(drawingMenus[position].drawing._id)

        // fetch drawing svgString from database
        runBlocking{
            val job = launch{
                DrawingSocketService.joinCurrentDrawingRoom()
            }
            job.join()
            DrawingSocketService.sendGetUpdateDrawingRequest(drawingMenus, position, destination)
            DrawingSocketService.setDrawingCommandSocketListeners()
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val drawingMenuViewHolder: DrawingMenuViewHolder

        init {
            val name = itemView.findViewById<TextView>(R.id.card_drawing_menu_name)
            val authorName = itemView.findViewById<TextView>(R.id.card_drawing_menu_author)
            val drawingDate = itemView.findViewById<TextView>(R.id.card_drawing_menu_date)
            val imageView = itemView.findViewById<ImageView>(R.id.card_drawing_menu_image)
            val lockIconView = itemView.findViewById<ImageView>(R.id.card_drawing_menu_privacy_icon)
            val authorImageView = itemView.findViewById<ImageView>(R.id.card_drawing_menu_author_image)
            val privacyLevel = itemView.findViewById<TextView>(R.id.card_drawing_menu_date_privacy)
            val popupMenu = itemView.findViewById<ImageButton>(R.id.card_drawing_menu_options)
            val collaborators = itemView.findViewById<TextView>(R.id.card_drawing_menu_collaborators)

            drawingMenuViewHolder = DrawingMenuViewHolder(name, authorName, drawingDate, imageView, lockIconView, authorImageView, privacyLevel, popupMenu, collaborators)

            // click listener for clicking on specific drawing
            itemView.setOnClickListener {
                val position: Int = bindingAdapterPosition

                if (drawingMenus[position].drawing.privacyLevel == PrivacyLevel.PROTECTED.toString()) {
                    // show password dialog
                    val title = "You're about to join ${drawingMenus[position].drawing.name}'s collaborative session."
                    val description = "The owner has set the privacy level of this drawing to protected. \n" +
                            "Enter the correct password to gain access to this session."
                    val passwordConfirmation = PasswordConfirmationBottomSheet(
                        activity,
                        drawingMenus[position].drawing.password,
                        title,
                        description,
                        "Join",
                        "Enter the session's password"
                    ) { openDrawing(bindingAdapterPosition, itemView.context) }
                    passwordConfirmation.show(activity.supportFragmentManager, "PasswordConfirmationBottomSheet")
                    return@setOnClickListener
                }

                openDrawing(bindingAdapterPosition, itemView.context)
            }

            popupMenu.setOnClickListener {
                val optionMenu = PopupMenu(it.context, it!!)
                optionMenu.inflate(R.menu.drawing_popup_menu)
                optionMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

                    when (item!!.itemId) {
                        R.id.edit_drawing -> {
                            val updateDrawingBS = EditDrawingBottomSheet(activity, drawingMenus[bindingAdapterPosition].drawing) { updatedDrawing -> updateDrawing(updatedDrawing, bindingAdapterPosition) }
                            updateDrawingBS.show(activity.supportFragmentManager, "EditDrawingBottomSheet")
                        }
                        R.id.delete_drawing -> {
                            val drawing = drawingMenus[bindingAdapterPosition].drawing
                            val title = "Are you sure you want to delete ${drawing.name}?"
                            val description = "All data will be lost and permanently erased from ColorImage. This action is irreversible."
                            val deleteConfirmation = ConfirmationBottomSheet({ deleteDrawing(drawing._id!!, bindingAdapterPosition) }, title, description, "DELETE", ButtonType.DELETE.toString())
                            deleteConfirmation.show(activity.supportFragmentManager, "ConfirmationBottomSheet")
                        }
                    }

                    true
                })
                optionMenu.show()
            }

            authorName.setOnClickListener {
                val ownerModel = drawingMenus[bindingAdapterPosition].drawing.ownerModel
                val ownerId = drawingMenus[bindingAdapterPosition].drawing.owner._id

                if (ownerModel == OwnerModel.USER.toString()) {
                    MyFragmentManager(activity).openWithData(R.id.main_gallery_fragment, UsersProfileFragment(), Constants.USERS.CURRENT_USER_ID_KEY, ownerId)
                } else {
                    MyFragmentManager(activity).openWithData(R.id.main_gallery_fragment, TeamsProfileFragment(), Constants.TEAMS.CURRENT_TEAM_ID_KEY, ownerId)
                }
            }
        }
    }
}