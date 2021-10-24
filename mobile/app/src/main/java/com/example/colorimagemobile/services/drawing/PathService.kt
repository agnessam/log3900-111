package com.example.colorimagemobile.services.drawing

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

// contains paint customizations and path's points
data class PaintPath(val paint: CustomPaint, val path: Path)

// Paint customizations
class CustomPaint() {
    private var paint: Paint = Paint()

    init {
        // default attributes
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.strokeWidth = 5F
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
    }

    fun setColor(newColor: Int) {
        paint.color = newColor
    }

    fun getPaint(): Paint {
        return paint
    }
}

// global path and paint holder
object PathService {
    private var paintPath: ArrayList<PaintPath> = arrayListOf()

    fun addPaintPath(newPaintPath: PaintPath) {
        paintPath.add(newPaintPath)
    }

    fun getPaintPath(): ArrayList<PaintPath> {
        return paintPath
    }
}