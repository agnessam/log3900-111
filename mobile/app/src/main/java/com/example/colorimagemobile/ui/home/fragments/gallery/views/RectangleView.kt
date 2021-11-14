package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import com.example.colorimagemobile.classes.toolsCommand.RectangleCommand
import com.example.colorimagemobile.models.RectangleData
import com.example.colorimagemobile.services.UUIDService
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.services.drawing.toolsAttribute.RectangleService
import com.example.colorimagemobile.services.drawing.toolsAttribute.RectangleStyle
import kotlin.math.abs

class RectangleView(context: Context?): CanvasView(context) {
    private var paintPath: PaintPath? = null
    private var rectangleCommand: RectangleCommand? = null
    private val rectangleType = "Rectangle"

    override fun createPathObject() {
        currentX = motionTouchEventX
        currentY = motionTouchEventY
        val id = UUIDService.generateUUID()
        var rectangleStyle = RectangleService.getBorderStyle()
        var fill = "none"
        var stroke = "none"
        var color = ColorService.getPrimaryColorAsInt()
        var secondaryColor = ColorService.getSecondaryColorAsInt()
        when(rectangleStyle){
            RectangleStyle.WITH_BORDER_FILL -> {
                fill = Integer.toHexString(color) // TODO IMPLEMENT PRIMARY AND SECONDARY COLORS
                stroke = Integer.toHexString(secondaryColor)
            }
            RectangleStyle.NO_BORDER ->{
                fill = Integer.toHexString(color)
            }
            RectangleStyle.ONLY_BORDER -> {
                stroke = Integer.toHexString(secondaryColor)
            }
        }

        var rectangleData = RectangleData(
            id = id,
            fill = fill,
            stroke = stroke,
            fillOpacity = "1",
            strokeOpacity = "1",
            strokeWidth = RectangleService.currentWidth,
            x = currentX.toInt(),
            y = currentY.toInt(),
            width = 0,
            height = 0
        )

        rectangleCommand = RectangleCommand(rectangleData)
    }

    override fun onTouchDown() {
        CanvasService.extraCanvas.save()
        createPathObject()
    }

    override fun onTouchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)

        // check if finger has moved for real
        if (dx >= touchTolerance || dy >= touchTolerance) {
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            rectangleCommand!!.setEndPoint(Point(currentX, currentY))
            rectangleCommand!!.execute()
        }
    }

    override fun onTouchUp() {
    }
}