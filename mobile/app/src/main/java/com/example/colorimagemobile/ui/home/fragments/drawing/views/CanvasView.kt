package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.example.colorimagemobile.services.drawing.CustomPaint
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.PathService
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService

abstract class CanvasView(context: Context?): View(context) {
    private lateinit var extraBitmap: Bitmap
    protected lateinit var extraCanvas: Canvas
    protected var paintPath: PaintPath = PaintPath(CustomPaint(), Path())

    protected var motionTouchEventX = 0f
    protected var motionTouchEventY = 0f
    protected var currentX = 0f
    protected var currentY = 0f
    protected val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)

        drawPreviousCanvas()
    }

    private fun drawPreviousCanvas() {
        for (paintPathItem in PathService.getPaintPath()) {
            extraCanvas.drawPath(paintPathItem.path, paintPathItem.brush.getPaint())
        }
    }

    // called for each render/finger movement
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
    }

    // when taking an action on canvas
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false

        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            // when user first touches the screen, set new paintPath
            MotionEvent.ACTION_DOWN -> {
                paintPath = PaintPath(CustomPaint(), Path())
                paintPath.brush.setColor(ColorService.getColor())
                onTouchDown()
            }

            MotionEvent.ACTION_MOVE -> onTouchMove()
            MotionEvent.ACTION_UP -> onTouchUp() // when user lifts finger
            else -> return false
        }

        return true
    }

    abstract fun onTouchDown()
    abstract fun onTouchMove()

    // can be overridden by children
    open fun onTouchUp() {
        PathService.addPaintPath(paintPath)
    }
}