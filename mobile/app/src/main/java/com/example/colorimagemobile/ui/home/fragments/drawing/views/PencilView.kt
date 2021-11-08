package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import android.graphics.Path
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.services.drawing.CustomPaint
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.PathService
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import kotlin.math.abs

class PencilView(context: Context?): CanvasView(context) {
    private var paintPath: PaintPath? = null
    private var pencilCommand: PencilCommand? = null

    private fun createObject() {
        paintPath = PaintPath(0, CustomPaint(), Path(), arrayListOf())
        paintPath!!.brush.setStrokeWidth(PencilService.getCurrentWidthAsFloat())
        paintPath!!.brush.setColor(ColorService.getColor())

        pencilCommand = PencilCommand(paintPath as PaintPath)
    }

    override fun onTouchDown() {
        createObject()

        paintPath!!.path.moveTo(motionTouchEventX, motionTouchEventY)

        currentX = motionTouchEventX
        currentY = motionTouchEventY

        pencilCommand!!.addPoint(Point(currentX, currentY))
        pencilCommand!!.execute();
    }

    override fun onTouchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)

        // check if finger has moved for real
        if (dx >= touchTolerance || dy >= touchTolerance) {
//            paintPath.path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY

//            extraCanvas.drawPath(paintPath.path, paintPath.brush.getPaint())
//            paintPath.points.add(Point((motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2))
        }

        invalidate()
    }

    override fun onTouchUp() {
//        pencilCommand!!.addPoint(Point(currentX, currentY))
        paintPath = null
    }
}