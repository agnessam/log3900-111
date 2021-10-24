package com.example.colorimagemobile.ui.home.fragments.drawing

import android.content.Context
import android.graphics.Canvas

class PencilView(context: Context?): CanvasView(context) {

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint);
    }

    override fun onTouchDown(pointX: Float, pointY: Float) {
        path.reset()
        path.moveTo(pointX, pointY)
    }

    override fun onTouchMove(pointX: Float, pointY: Float) {
        path.lineTo(pointX, pointY)
    }
}