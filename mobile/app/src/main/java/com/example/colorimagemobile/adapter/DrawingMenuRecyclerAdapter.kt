package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuViewHolder
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast

class DrawingMenuRecyclerAdapter(drawings: ArrayList<DrawingMenuData>): RecyclerView.Adapter<DrawingMenuRecyclerAdapter.ViewHolder>() {

    val drawingMenus: ArrayList<DrawingMenuData> = drawings

    // create card view and sets its contents format
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawingMenuRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout. recycler_drawing_menu, parent, false)
        return ViewHolder(view)
    }

    // populate each data to cardview
    override fun onBindViewHolder(holder: DrawingMenuRecyclerAdapter.ViewHolder, position: Int) {
        holder.drawingMenuViewHolder.name.text = drawingMenus[position].name
        holder.drawingMenuViewHolder.image.setImageBitmap(drawingMenus[position].imageBitmap)
    }

    // identify how many item we pass to view holder
    override fun getItemCount(): Int {
        return drawingMenus.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val drawingMenuViewHolder: DrawingMenuViewHolder

        init {
            val name = itemView.findViewById<TextView>(R.id.card_drawing_menu_name)
            val imageView = itemView.findViewById<ImageView>(R.id.card_drawing_menu_image)

            drawingMenuViewHolder = DrawingMenuViewHolder(name, imageView)

            // click listener for clicking on specific drawing
            itemView.setOnClickListener {
                val position: Int = bindingAdapterPosition
                printToast(itemView.context, "clicked on $position")
            }
        }
    }
}