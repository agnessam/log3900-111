package com.example.colorimagemobile.ui.home.fragments.drawing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.PathService

class EraserView(context: Context?): CanvasView(context) {

    override fun onDraw(canvas: Canvas) {
        customPaint.setColor(Color.WHITE)
        val paintPath = PaintPath(customPaint, path)
        PathService.addPaintPath(paintPath)
        super.onDraw(canvas)
    }

    override fun onTouchDown(pointX: Float, pointY: Float) {
        path.moveTo(pointX, pointY)
    }

    override fun onTouchMove(pointX: Float, pointY: Float) {
        path.lineTo(pointX, pointY)
    }

    override fun onTouchUp() {

    }
}