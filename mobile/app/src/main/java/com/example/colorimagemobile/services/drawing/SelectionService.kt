package com.example.colorimagemobile.services.drawing

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape

// Helper to draw selection box to canvas
object SelectionService {
    var selectionBox: LayerDrawable = LayerDrawable(arrayOf<Drawable>())
    var selectionRectangle: ShapeDrawable = ShapeDrawable(RectShape())
    private var paint: Paint = Paint()


    fun initSelectionRectangle() {
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.MITER
        paint.strokeWidth = 1F
        paint.color = Color.BLUE
        var intervals = arrayOf(10F, 10F)
//        paint.pathEffect = DashPathEffect(intervals.toFloatArray(), 0F)

        selectionRectangle.paint.set(paint)
    }

    fun setSelectionBounds(left: Int, top: Int, right: Int, bottom: Int) {
        var selectionBounds = Rect()
        selectionBounds.set(left, top, right, bottom)

        selectionRectangle.setBounds(selectionBounds)
        selectionBox.addLayer(selectionRectangle)
//        var topLeft: ShapeDrawable = ShapeDrawable(OvalShape())
//        topLeft.
    }
}