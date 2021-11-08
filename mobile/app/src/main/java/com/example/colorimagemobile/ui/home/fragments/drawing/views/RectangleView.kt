package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import com.example.colorimagemobile.Interface.Editor
import com.example.colorimagemobile.Shape.DrawingPreview
import com.example.colorimagemobile.Shape.ShapeBuilder
import com.example.colorimagemobile.Shape.ShapeType
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService

class RectangleView(context: Context?): DrawingPreview(context)  {
    init {

        currentShape!!.paint.strokeWidth = PencilService.getCurrentWidthAsFloat()
    }

}