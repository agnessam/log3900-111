package com.example.colorimagemobile.services.drawing

import android.graphics.Paint
import com.example.colorimagemobile.Shape.ShapeAndPaint
import com.example.colorimagemobile.Shape.ShapeBuilder
import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import com.example.colorimagemobile.services.drawing.toolsAttribute.RectangleService
import kotlin.collections.ArrayList




// Paint/brush customizations
class CustomPaint() {
    var currentShapeBuilder: ShapeBuilder? = null
    private var paint: Paint = Paint()

    init {
        createPaint()
    }

    fun createPaint() : Paint {

        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND

        paint.alpha = 255

        setColor(ColorService.getColor())
        if (ToolTypeService.getCurrentToolType().value == ToolType.RECTANGLE) {
            setStrokeWidth(RectangleService.getCurrentWidthAsFloat())
        }
        else{
            setStrokeWidth(PencilService.getCurrentWidthAsFloat())
        }

        return paint
    }

    fun setColor(newColor: Int) {
        paint.color = newColor
    }

    fun setStrokeWidth(newWidth: Float) {
        paint.strokeWidth = newWidth
    }

//    fun getPaint(): Paint {
//        return paint
//    }
}

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