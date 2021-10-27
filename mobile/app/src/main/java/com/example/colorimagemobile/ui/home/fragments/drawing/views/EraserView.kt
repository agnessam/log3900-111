package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import android.graphics.Color
import com.example.colorimagemobile.services.drawing.toolsAttribute.EraserService

class EraserView(context: Context?): CanvasView(context) {

    init {
        paintPath.brush.setColor(Color.WHITE)
        paintPath.brush.setStrokeWidth(EraserService.getCurrentWidthAsFloat())
    }

    override fun onTouchDown(pointX: Float, pointY: Float) {
        paintPath.brush.setColor(Color.WHITE)
        paintPath.brush.setStrokeWidth(EraserService.getCurrentWidthAsFloat())
        paintPath.path.moveTo(pointX, pointY)
    }

    override fun onTouchMove(pointX: Float, pointY: Float) {
        paintPath.path.lineTo(pointX, pointY)
    }
}