package com.example.colorimagemobile.ui.home.fragments.drawing.views

import android.content.Context
import android.graphics.Path
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.interfaces.InProgressPencil
import com.example.colorimagemobile.interfaces.ToolData
import com.example.colorimagemobile.services.UUIDService
import com.example.colorimagemobile.services.drawing.*
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import com.example.colorimagemobile.services.socket.DrawingSocketService
import kotlin.math.abs

class PencilView(context: Context?): CanvasView(context) {
    private var paintPath: PaintPath? = null
    private var pencilCommand: PencilCommand? = null
    private var inProgressPencil: InProgressPencil? = null
    private val pencilType = "Pencil"

    private fun createObject() {
        paintPath = PaintPath(UUIDService.generateUUID(), CustomPaint(), Path(), arrayListOf())
        paintPath!!.brush.setStrokeWidth(PencilService.getCurrentWidthAsFloat())
        paintPath!!.brush.setColor(ColorService.getColorAsInt())

        pencilCommand = PencilCommand(paintPath as PaintPath)
    }

    private fun updateCanvas() {
        pencilCommand!!.addPoint(Point(currentX, currentY))
        pencilCommand!!.execute()
    }

    override fun onTouchDown() {
        createObject()
        paintPath!!.path.moveTo(motionTouchEventX, motionTouchEventY)

        currentX = motionTouchEventX
        currentY = motionTouchEventY
        updateCanvas()

        val id = UUIDService.generateUUID()
        val point = Point(currentX, currentY)

        val pencil = ToolData(
            id = id,
            pointsList = arrayListOf(point),
            fill = "none",
            stroke = ColorService.getColorAsString(),
            fillOpacity = "1",
            strokeOpacity = "1",
            strokeWidth = paintPath!!.brush.getPaint().strokeWidth.toInt()
        )

        inProgressPencil = InProgressPencil(id, point)
        DrawingSocketService.sendInProgressDrawingCommand(pencil, pencilType)
    }

    override fun onTouchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)

        // check if finger has moved for real
        if (dx >= touchTolerance || dy >= touchTolerance) {
            paintPath!!.path.quadTo(currentX, currentY, (motionTouchEventX + currentX) / 2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY

            updateCanvas()
            inProgressPencil!!.point = Point(motionTouchEventX, motionTouchEventY)
            DrawingSocketService.sendInProgressDrawingCommand(inProgressPencil!!, pencilType)
        }
    }

    override fun onTouchUp() {
        updateCanvas()
        PathService.addPaintPath(paintPath as PaintPath)
        paintPath = null
    }
}