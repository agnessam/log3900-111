package com.example.colorimagemobile.services.drawing

import android.graphics.Color
import android.graphics.DashPathEffect
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
    private lateinit var selectionRectangle: ShapeDrawable
    private lateinit var topCtrl: LayerDrawable
    private lateinit var leftCtrl: LayerDrawable
    private lateinit var rightCtrl: LayerDrawable
    private lateinit var bottomCtrl: LayerDrawable
    private lateinit var topLeftCtrl: LayerDrawable
    private lateinit var topRightCtrl: LayerDrawable
    private lateinit var bottomLeftCtrl: LayerDrawable
    private lateinit var bottomRightCtrl: LayerDrawable

    private var fillIndex: Int = -1
    private var borderIndex: Int = -1

    private val pointWidth: Int = 5

    fun initSelectionRectangle() {
        var paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.MITER
        paint.strokeWidth = 1F
        paint.color = Color.BLACK
        paint.alpha = 255
        var intervals = arrayOf(10F, 10F)
        paint.pathEffect = DashPathEffect(intervals.toFloatArray(), 0F)

        selectionRectangle = ShapeDrawable(RectShape())
        selectionRectangle.paint.set(paint)
    }

    fun setSelectionBounds(left: Int, top: Int, right: Int, bottom: Int) {
        var selectionBounds = Rect()
        selectionBounds.set(left, top, right, bottom)

        selectionRectangle.setBounds(selectionBounds)
        selectionBox.addLayer(selectionRectangle)

        var width = right - left
        var height = bottom - top
        var horizontalCenter = left + width / 2
        var verticalCenter = top + height / 2

        topCtrl = setResizingPoint(horizontalCenter, top)
        leftCtrl = setResizingPoint(left, verticalCenter)
        rightCtrl = setResizingPoint(right, verticalCenter)
        bottomCtrl = setResizingPoint(horizontalCenter, bottom)
        topLeftCtrl = setResizingPoint(left, top)
        topRightCtrl = setResizingPoint(right, top)
        bottomLeftCtrl = setResizingPoint(left, bottom)
        bottomRightCtrl = setResizingPoint(right, bottom)


        selectionBox.addLayer(topCtrl)
        selectionBox.addLayer(leftCtrl)
        selectionBox.addLayer(rightCtrl)
        selectionBox.addLayer(bottomCtrl)
        selectionBox.addLayer(topLeftCtrl)
        selectionBox.addLayer(topRightCtrl)
        selectionBox.addLayer(bottomLeftCtrl)
        selectionBox.addLayer(bottomRightCtrl)
    }

    private fun setResizingPoint(x: Int, y: Int): LayerDrawable {
        var borderShape = ShapeDrawable(RectShape())
        var fillShape = ShapeDrawable(RectShape())

        var borderPaint = Paint()
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeJoin = Paint.Join.MITER
        borderPaint.color = Color.BLACK
        borderPaint.strokeWidth = 1F
        borderPaint.alpha = 255
        borderShape.paint.set(borderPaint)

        var fillPaint = Paint()
        fillPaint.style = Paint.Style.FILL
        fillPaint.strokeJoin = Paint.Join.MITER
        fillPaint.color = Color.WHITE
        fillPaint.strokeWidth = 1F
        fillPaint.alpha = 255
        fillShape.paint.set(fillPaint)

        borderShape.setBounds(
            x - pointWidth,
            y - pointWidth,
            x + pointWidth,
            y + pointWidth
        )
        fillShape.setBounds(
            x - pointWidth,
            y - pointWidth,
            x + pointWidth,
            y + pointWidth
        )

        var pointShape = LayerDrawable(arrayOf<Drawable>())
        fillIndex = pointShape.addLayer(fillShape)
        borderIndex = pointShape.addLayer(borderShape)

        return pointShape
    }

    fun clearSelection() {
        selectionBox = LayerDrawable(arrayOf<Drawable>())
    }
}