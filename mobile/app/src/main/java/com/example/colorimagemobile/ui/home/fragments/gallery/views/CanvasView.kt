package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.services.drawing.*

abstract class CanvasView(context: Context?): View(context) {
    protected var motionTouchEventX = 0f
    protected var motionTouchEventY = 0f
    protected var currentX = 0f
    protected var currentY = 0f
    protected val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    protected var counter = 0

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        CanvasService.setWidth(width)
        CanvasService.setHeight(height)
//        CanvasService.drawPreviousCanvas()

        invalidateCanvasListener()
    }

    // to define our paintPath object
    protected abstract fun createPathObject()

    // emitter to update canvas
    private fun invalidateCanvasListener() {
        CanvasUpdateService.getLiveData().observe(context as LifecycleOwner, {
            invalidate()
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(CanvasService.extraBitmap, 0f, 0f, null)
        CanvasService.layerDrawable.draw(canvas)
//        var shapeDrawable: ShapeDrawable = ShapeDrawable(RectShape())
//        shapeDrawable.setBounds(0, 0, 50, 50)
//        shapeDrawable.getPaint().setColor(Color.parseColor("#009944"))
//        shapeDrawable.draw(canvas)

//        if(counter != 0){
//
//            var shapeDrawable: ShapeDrawable = ShapeDrawable(RectShape())
//
//            shapeDrawable.setBounds(counter, counter, counter + 90, counter + 90)
//            shapeDrawable.getPaint().setColor(Color.parseColor("#009944"))
//            CanvasService.layerDrawable.addLayer(shapeDrawable)
//            if(counter == 50){
//
//                val rectangleDrawable = CanvasService.layerDrawable.getDrawable(3)
//                rectangleDrawable.setBounds(300,10, 500, 210)
//            }
//            else if(counter == 100){
//
//                val rectangleDrawable = CanvasService.layerDrawable.getDrawable(3)
//                rectangleDrawable.setBounds(100,10, 200, 310)
//            }
//        }
//        CanvasService.layerDrawable.draw(canvas)
//        counter = if (counter >= 100) 0 else counter + 10
    }

    // when taking an action on canvas
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false

        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            // when user first touches the screen, set new paintPath
            MotionEvent.ACTION_DOWN -> onTouchDown()
            MotionEvent.ACTION_MOVE -> onTouchMove()
            MotionEvent.ACTION_UP -> onTouchUp() // when user lifts finger
            else -> return false
        }

        return true
    }

    abstract fun onTouchDown()
    abstract fun onTouchMove()
    abstract fun onTouchUp()
}