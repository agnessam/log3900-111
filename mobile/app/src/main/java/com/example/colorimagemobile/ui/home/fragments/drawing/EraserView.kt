package com.example.colorimagemobile.ui.home.fragments.drawing

import android.content.Context
import android.graphics.Color
import com.example.colorimagemobile.services.drawing.PathService

class EraserView(context: Context?): CanvasView(context) {

    init {
        paintPath.paint.setColor(Color.WHITE)
        paintPath.paint.setStrokeWidth(20F)
    }

    override fun onTouchDown(pointX: Float, pointY: Float) {
        PathService.addPaintPath(paintPath)
        paintPath.path.moveTo(pointX, pointY)
    }

    override fun onTouchMove(pointX: Float, pointY: Float) {
        paintPath.path.lineTo(pointX, pointY)
    }

    override fun onTouchUp() {
    }
}