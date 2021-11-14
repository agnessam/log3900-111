package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import com.example.colorimagemobile.classes.toolsCommand.EllipseCommand
import com.example.colorimagemobile.models.EllipseData
import com.example.colorimagemobile.services.UUIDService
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.services.drawing.toolsAttribute.EllipseService
import com.example.colorimagemobile.services.drawing.toolsAttribute.EllipseStyle
import kotlin.math.abs

class EllipseView(context: Context?): CanvasView(context) {
    private var paintPath: PaintPath? = null
    private var ellipseCommand: EllipseCommand? = null

    override fun createPathObject() {
        currentX = motionTouchEventX
        currentY = motionTouchEventY
        val id = UUIDService.generateUUID()
        var ellipseStyle = EllipseService.getBorderStyle()
        var fill = "none"
        var stroke = "none"
        var color = ColorService.getPrimaryColorAsInt()
        when(ellipseStyle){
            EllipseStyle.WITH_BORDER_FILL -> {
                fill = Integer.toHexString(color) // TODO IMPLEMENT PRIMARY AND SECONDARY COLORS
                stroke = Integer.toHexString(color)
            }
            EllipseStyle.NO_BORDER ->{
                stroke = Integer.toHexString(color)
            }
            EllipseStyle.ONLY_BORDER -> {
                fill = Integer.toHexString(color)
            }
        }

        var ellipseData = EllipseData(
            id = id,
            fill = fill,
            stroke = stroke,
            fillOpacity = "1",
            strokeOpacity = "1",
            strokeWidth = EllipseService.currentWidth,
            x = currentX.toInt(),
            y = currentY.toInt(),
            width = 0,
            height = 0
        )

        ellipseCommand = EllipseCommand(ellipseData)
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
            ellipseCommand!!.setEndPoint(Point(currentX, currentY))
            ellipseCommand!!.execute()
        }
    }

    override fun onTouchUp() {
    }
}