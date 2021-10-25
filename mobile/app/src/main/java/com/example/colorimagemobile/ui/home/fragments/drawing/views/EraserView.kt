package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import android.graphics.Color
import com.example.colorimagemobile.services.drawing.toolsAttribute.EraserService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

class EraserView(context: Context?): CanvasView(context) {

    init {
        paintPath.paint.setColor(Color.WHITE)
        paintPath.paint.setStrokeWidth(EraserService.getCurrentWidthAsFloat())
    }

    override fun onTouchDown(pointX: Float, pointY: Float) {
        paintPath.paint.setColor(Color.WHITE)
        paintPath.paint.setStrokeWidth(EraserService.getCurrentWidthAsFloat())
        printMsg(EraserService.getCurrentWidthAsFloat().toString())
        paintPath.path.moveTo(pointX, pointY)
    }

    override fun onTouchMove(pointX: Float, pointY: Float) {
        paintPath.path.lineTo(pointX, pointY)
    }
}