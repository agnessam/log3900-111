package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import com.example.colorimagemobile.Interface.Editor
import com.example.colorimagemobile.Shape.DrawingPreview
import com.example.colorimagemobile.Shape.ShapeBuilder
import com.example.colorimagemobile.Shape.ShapeType
//import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import com.example.colorimagemobile.ui.home.fragments.drawing.DrawingFragment
import kotlin.math.abs

class PencilView(context: Context?): DrawingPreview(context) {


    init {
//        DrawingFragment().mShapeBuilder = ShapeBuilder()
//        DrawingFragment().mShapeBuilder!!.withShapeType(ShapeType.BRUSH)
//        DrawingFragment().viewEditor!!.setShape(DrawingFragment().mShapeBuilder)
        currentShape!!.paint.strokeWidth = PencilService.getCurrentWidthAsFloat()
    }

//    override fun onTouchDown() {
//        paintPath.brush.setStrokeWidth(PencilService.getCurrentWidthAsFloat())
//        paintPath.path.moveTo(motionTouchEventX, motionTouchEventY)
//        paintPath.points.add(Point(motionTouchEventX, motionTouchEventY))
//
//        currentX = motionTouchEventX
//        currentY = motionTouchEventY
//    }
//
//    override fun onTouchMove() {
//        val dx = abs(motionTouchEventX - currentX)
//        val dy = abs(motionTouchEventY - currentY)
//
//        // check if finger has moved for real
//        if (dx >= touchTolerance || dy >= touchTolerance) {
//            paintPath.path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
//            currentX = motionTouchEventX
//            currentY = motionTouchEventY
//            extraCanvas.drawPath(paintPath.path, paintPath.brush.getPaint())
//            paintPath.points.add(Point((motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2))
//        }
//
//        invalidate()
//    }
}