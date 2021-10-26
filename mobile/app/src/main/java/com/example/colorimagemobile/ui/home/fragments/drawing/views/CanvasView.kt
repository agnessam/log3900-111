package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import com.example.colorimagemobile.services.drawing.CustomPaint
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.PathService

abstract class CanvasView(context: Context?): View(context) {
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    protected var paintPath: PaintPath = PaintPath(CustomPaint(), Path())

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        if (!::extraBitmap.isInitialized) extraBitmap.recycle()

        drawPreviousCanvas()
    }

    private fun drawPreviousCanvas() {
        for (paintPathItem in PathService.getPaintPath()) {
            extraCanvas.drawPath(paintPathItem.path, paintPathItem.paint.getPaint())
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
        extraCanvas.drawPath(paintPath.path, paintPath.paint.getPaint())
    }

    // when taking an action on canvas
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false

        val pointX = event.x
        val pointY = event.y

        when (event.action) {
            // when user first touches the screen
            MotionEvent.ACTION_DOWN -> {
                // reset paintPath
                paintPath = PaintPath(CustomPaint(), Path())
                onTouchDown(pointX, pointY)
            }

            MotionEvent.ACTION_MOVE -> onTouchMove(pointX, pointY)

            // when user lifts finger
            MotionEvent.ACTION_UP -> onTouchUp()

            else -> return false
        }

        postInvalidate()
        return true
    }

    abstract fun onTouchDown(pointX: Float, pointY: Float)
    abstract fun onTouchMove(pointX: Float, pointY: Float)

    // can be overridden by children
    open fun onTouchUp() {
        PathService.addPaintPath(paintPath)
    }
}