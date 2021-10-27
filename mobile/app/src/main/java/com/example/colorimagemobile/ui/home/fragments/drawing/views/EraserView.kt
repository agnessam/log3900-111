package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import android.graphics.Color
import com.example.colorimagemobile.services.drawing.toolsAttribute.EraserService

class EraserView(context: Context?): CanvasView(context) {

    init {
        paintPath.brush.setColor(Color.WHITE)
        paintPath.brush.setStrokeWidth(EraserService.getCurrentWidthAsFloat())
    }

    override fun onTouchDown() {
//        paintPath.brush.setColor(Color.WHITE)
//        paintPath.brush.setStrokeWidth(EraserService.getCurrentWidthAsFloat())
//        paintPath.path.moveTo(pointX, pointY)
    }

    override fun onTouchMove() {
//        paintPath.path.lineTo(pointX, pointY)
    }
}