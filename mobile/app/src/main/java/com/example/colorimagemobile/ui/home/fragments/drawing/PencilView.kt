package com.example.colorimagemobile.ui.home.fragments.drawing

import android.content.Context
import com.example.colorimagemobile.services.drawing.PathService

class PencilView(context: Context?): CanvasView(context) {

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