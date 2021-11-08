package com.example.colorimagemobile.services.drawing

import android.graphics.Bitmap
import android.graphics.Canvas
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

object CanvasService {
    lateinit var extraBitmap: Bitmap
    lateinit var extraCanvas: Canvas

    private var width = 0
    private var height = 0

    fun setWidth(newWidth: Int) {
        width = newWidth
    }

    fun setHeight(newHeight: Int) {
        height = newHeight
    }

    // maybe get previous bitmap and draw it instead of drawing everything
    fun drawPreviousCanvas() {
        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)

        for (paintPathItem in PathService.getPaintPath()) {
            extraCanvas.drawPath(paintPathItem.path, paintPathItem.brush.getPaint())
        }
    }
}