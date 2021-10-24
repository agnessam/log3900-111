package com.example.colorimagemobile.ui.home.fragments.drawing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

class EraserView(context: Context?): CanvasView(context) {

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        printMsg("ERASERR")
    }


    override fun onTouchDown(pointX: Float, pointY: Float) {
    }

    override fun onTouchMove(pointX: Float, pointY: Float) {
    }
}