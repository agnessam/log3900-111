package com.example.colorimagemobile.services.drawing

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand

object DrawingObjectManager {
    var layerDrawable: LayerDrawable = LayerDrawable(arrayOf<Drawable>())

}