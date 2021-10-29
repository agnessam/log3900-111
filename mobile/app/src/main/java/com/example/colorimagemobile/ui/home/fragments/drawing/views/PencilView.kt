package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import kotlin.math.abs

class PencilView(context: Context?): CanvasView(context) {

    init {
        paintPath.brush.setStrokeWidth(PencilService.getCurrentWidthAsFloat())
    }

    override fun onTouchDown() {
        paintPath.brush.setStrokeWidth(PencilService.getCurrentWidthAsFloat())
        paintPath.path.moveTo(motionTouchEventX, motionTouchEventY)
        paintPath.points.add(Point(motionTouchEventX, motionTouchEventY))

        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    override fun onTouchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)

        // check if finger has moved for real
        if (dx >= touchTolerance || dy >= touchTolerance) {
            paintPath.path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            extraCanvas.drawPath(paintPath.path, paintPath.brush.getPaint())
            paintPath.points.add(Point((motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2))
        }

        invalidate()
    }
}