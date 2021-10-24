package com.example.colorimagemobile.ui.home.fragments.drawing

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View

abstract class CanvasView(context: Context?): View(context) {
    protected lateinit var extraCanvas: Canvas
    protected lateinit var extraBitmap: Bitmap

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)

        if (!::extraBitmap.isInitialized) extraBitmap.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
    }
}