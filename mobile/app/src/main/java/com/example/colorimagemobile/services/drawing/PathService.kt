package com.example.colorimagemobile.services.drawing

import com.example.colorimagemobile.Shape.ShapeAndPaint
import kotlin.collections.ArrayList


// global path and paint holder
object PathService {
    private var shapeAndPaint: ArrayList<ShapeAndPaint> = arrayListOf()

    fun addPaintPath(newPaintPath: ShapeAndPaint) {
        shapeAndPaint.add(newPaintPath)
    }

    fun getPaintPath(): ArrayList<ShapeAndPaint> {
        return shapeAndPaint
    }

    fun removeByID(id: Int) {
        shapeAndPaint = shapeAndPaint.filterIndexed { _, ShapeAndPaint -> ShapeAndPaint.id != id  } as ArrayList<ShapeAndPaint>
    }

    fun removeAll() {
        shapeAndPaint.clear()
    }

}

// generate new id [temporary?]
object IDGenerator {
    private var id = 0

    fun getNewId(): Int {
        id++
        return id
    }
}