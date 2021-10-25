package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import android.graphics.Path
import com.example.colorimagemobile.services.drawing.PathService
import com.example.colorimagemobile.services.drawing.ToolSettingsService
import androidx.appcompat.app.AppCompatActivity
import com.example.colorimagemobile.services.drawing.CustomPaint
import com.example.colorimagemobile.services.drawing.PaintPath

class PencilView(context: Context?): CanvasView(context) {

    private var currentWidth: Float = 0.0f

    init {
        ToolSettingsService.getCurrentWidth().observe((context as AppCompatActivity?)!!, {
            currentWidth = it.toFloat()
            paintPath.paint.setStrokeWidth(currentWidth)
        })
    }

    override fun onTouchDown(pointX: Float, pointY: Float) {
        paintPath.path.moveTo(pointX, pointY)
    }

    override fun onTouchMove(pointX: Float, pointY: Float) {
        paintPath.path.lineTo(pointX, pointY)
    }

    override fun onTouchUp() {
        // add paintPath and reset it
        PathService.addPaintPath(paintPath)
        paintPath = PaintPath(CustomPaint(), Path())
        paintPath.paint.setStrokeWidth(currentWidth)
    }
}