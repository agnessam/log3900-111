package com.example.colorimagemobile.models.recyclerAdapters

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView

/* holds data of drawing displayed in Gallery */

// our data to display
data class DrawingMenuData(val name: String, val imageBitmap: Bitmap)

// the layout elements in which we assign our data
data class DrawingMenuViewHolder(val name: TextView, val image: ImageView)
