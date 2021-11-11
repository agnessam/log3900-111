package com.example.colorimagemobile.services.drawing

import android.graphics.Bitmap
import android.graphics.Canvas
import com.example.colorimagemobile.utils.Constants

object CanvasService {
    lateinit var extraBitmap: Bitmap
    lateinit var extraCanvas: Canvas

    private var width = Constants.DRAWING.MAX_WIDTH
    private var height = Constants.DRAWING.MAX_HEIGHT

    fun getWidth(): Int {
        return width
    }

    fun getHeight(): Int {
        return height
    }

    fun setWidth(newWidth: Int) {
        width = newWidth
    }

    fun setHeight(newHeight: Int) {
        height = newHeight
    }

    fun updateCanvasColor(color: Int) {
        extraCanvas.drawColor(color)
    }

    // initialize and create new canvas/bitmap
    fun createNewBitmap() {
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
    }

    // pass bitmap retrieved/calculated from Server data
    fun createExistingBitmap(newBitmap: Bitmap) {
        extraBitmap = newBitmap
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawBitmap(extraBitmap, 0f, 0f, null)
    }

    // maybe get previous bitmap and draw it instead of drawing everything
    fun drawPreviousCanvas() {
        for (paintPathItem in PathService.getPaintPath()) {
            extraCanvas.drawPath(paintPathItem.path, paintPathItem.brush.getPaint())
        }
    }
}