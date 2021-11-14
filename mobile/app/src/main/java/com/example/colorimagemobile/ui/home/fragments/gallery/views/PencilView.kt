package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.models.InProgressPencil
import com.example.colorimagemobile.models.PencilData
import com.example.colorimagemobile.models.ToolData
import com.example.colorimagemobile.services.UUIDService
import com.example.colorimagemobile.services.drawing.*
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import com.example.colorimagemobile.services.socket.DrawingSocketService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import kotlin.math.abs

class PencilView(context: Context?): CanvasView(context) {

    private var pencilCommand: PencilCommand? = null
    private var inProgressPencil: InProgressPencil? = null
    private val pencilType = "Pencil"
    private lateinit var pencil: PencilData


    override fun createPathObject() {
        currentX = motionTouchEventX
        currentY = motionTouchEventY

        val id = UUIDService.generateUUID()
        val point = Point(currentX, currentY)

        pencil = PencilData(
            id = id,
            fill = "none",
            stroke = ColorService.getColorAsString(),
            fillOpacity = "1",
            strokeOpacity = "1",
            strokeWidth = PencilService.currentWidth,
            pointsList = arrayListOf(point),
        )
        // supposed to put the exact width and height of the path drawn...... but wtf how
        val pathShape = PathShape(Path(),
            CanvasService.extraCanvas.width.toFloat(), CanvasService.extraCanvas.height.toFloat())

        var shapeDrawable = ShapeDrawable(pathShape)
        var layerIndex = CanvasService.layerDrawable.addLayer(shapeDrawable)
        pencilCommand = PencilCommand(pencil, layerIndex)
//        pencilCommand.pencil.path.moveTo(motionTouchEventX, motionTouchEventY)
//        pencilCommand!!.path.moveTo(motionTouchEventX, motionTouchEventY)
    }

    private fun updateCanvas() {
        pencilCommand!!.addPoint(currentX, currentY)
        pencilCommand!!.execute()
    }

    override fun onTouchDown() {
        CanvasService.extraCanvas.save()
        createPathObject()

        updateCanvas()
//        inProgressPencil = InProgressPencil(pencil.id, Point(currentX, currentY))
//        DrawingSocketService.sendInProgressDrawingCommand(pencil, pencilType)
    }

    override fun onTouchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)

        // check if finger has moved for real
        if (dx >= touchTolerance || dy >= touchTolerance) {
            pencilCommand!!.addPoint(currentX, currentY)
            currentX = motionTouchEventX
            currentY = motionTouchEventY

            updateCanvas()
//            inProgressPencil!!.point = Point(motionTouchEventX, motionTouchEventY)
//            DrawingSocketService.sendInProgressDrawingCommand(inProgressPencil!!, pencilType)
        }
    }

    override fun onTouchUp() {
        updateCanvas()
//        PathService.addPaintPath(paintPath as PaintPath)
    }
}