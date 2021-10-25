package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import android.graphics.Path
import com.example.colorimagemobile.services.drawing.PathService
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import androidx.appcompat.app.AppCompatActivity
import com.example.colorimagemobile.services.drawing.CustomPaint
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.ToolService

class PencilView(context: Context?): CanvasView(context) {

    init {
        paintPath.paint.setStrokeWidth(PencilService.getCurrentWidthAsFloat())
    }

    override fun onTouchDown(pointX: Float, pointY: Float) {
        paintPath.paint.setStrokeWidth(PencilService.getCurrentWidthAsFloat())
        paintPath.path.moveTo(pointX, pointY)
    }

    override fun onTouchMove(pointX: Float, pointY: Float) {
        paintPath.path.lineTo(pointX, pointY)
    }
}