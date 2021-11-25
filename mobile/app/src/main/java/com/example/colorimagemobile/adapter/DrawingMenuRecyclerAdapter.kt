package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.DateFormatter
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuViewHolder
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.ui.home.fragments.gallery.GalleryDrawingFragment

class DrawingMenuRecyclerAdapter(drawings: ArrayList<DrawingMenuData>, val destination: Int): RecyclerView.Adapter<DrawingMenuRecyclerAdapter.ViewHolder>() {

    val drawingMenus: ArrayList<DrawingMenuData> = drawings

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawingMenuRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout. recycler_drawing_menu, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrawingMenuRecyclerAdapter.ViewHolder, position: Int) {
        holder.drawingMenuViewHolder.name.text = drawingMenus[position].drawing.name
        holder.drawingMenuViewHolder.authorName.text = drawingMenus[position].drawing.ownerId
        holder.drawingMenuViewHolder.drawingDate.text = DateFormatter.getDate(drawingMenus[position].drawing.createdAt!!)
        holder.drawingMenuViewHolder.image.setImageBitmap(drawingMenus[position].imageBitmap)
    }

    override fun getItemCount(): Int { return drawingMenus.size }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val drawingMenuViewHolder: DrawingMenuViewHolder

        init {
            val name = itemView.findViewById<TextView>(R.id.card_drawing_menu_name)
            val authorName = itemView.findViewById<TextView>(R.id.card_drawing_menu_author)
            val drawingDate = itemView.findViewById<TextView>(R.id.card_drawing_menu_date)
            val imageView = itemView.findViewById<ImageView>(R.id.card_drawing_menu_image)

            drawingMenuViewHolder = DrawingMenuViewHolder(name, authorName, drawingDate, imageView)

            // click listener for clicking on specific drawing
            itemView.setOnClickListener {
                val position: Int = bindingAdapterPosition

                // set clicked bitmap to canvas
                DrawingObjectManager.createDrawableObjects(drawingMenus[position].svgString)

                DrawingService.setCurrentDrawingID(drawingMenus[position].drawing._id)
                MyFragmentManager(itemView.context as FragmentActivity).open(destination, GalleryDrawingFragment())
                CanvasUpdateService.invalidate()
            }
        }
    }
}