package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import com.example.colorimagemobile.services.drawing.toolsAttribute.EraserService

class EraserView(context: Context?): CanvasView(context) {
    private var currentWidth: Float = EraserService.getCurrentWidth().value!!.toFloat()

    init {
        paintPath.paint.setColor(Color.WHITE)
        paintPath.paint.setStrokeWidth(currentWidth)

        EraserService.getCurrentWidth().observe((context as AppCompatActivity?)!!, {
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
        super.onTouchUp()
        paintPath.paint.setStrokeWidth(currentWidth)
        paintPath.paint.setColor(Color.WHITE)
    }
}