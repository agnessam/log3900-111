package com.example.colorimagemobile.ui.home.fragments.drawing

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

abstract class CanvasView(context: Context?): View(context) {
    protected lateinit var extraCanvas: Canvas
    protected lateinit var paint: Paint
    protected lateinit var extraBitmap: Bitmap
    protected var path: Path = Path()

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(Color.rgb(255, 255, 255))
        paint = Paint()

        if (!::extraBitmap.isInitialized) extraBitmap.recycle()

        setUpPaint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
    }

    // when taking an action on canvas
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false

        val pointX = event.x
        val pointY = event.y

        when (event.action) {
            // when user first touches the screen
            MotionEvent.ACTION_DOWN -> onTouchDown(pointX, pointY)

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
    private fun onTouchUp() { path.reset() }

    private fun setUpPaint() {
        paint.color = Color.GREEN
        paint.isAntiAlias = true
        paint.strokeWidth = 5F
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
    }
}