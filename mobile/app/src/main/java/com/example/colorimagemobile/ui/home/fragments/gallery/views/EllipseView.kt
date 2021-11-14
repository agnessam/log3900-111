package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import com.example.colorimagemobile.classes.toolsCommand.EllipseCommand
import com.example.colorimagemobile.models.EllipseData
import com.example.colorimagemobile.services.UUIDService
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.services.drawing.toolsAttribute.EllipseService
import kotlin.math.abs

class EllipseView(context: Context?): CanvasView(context) {
    private var paintPath: PaintPath? = null
    private var ellipseCommand: EllipseCommand? = null

    override fun createPathObject() {
        currentX = motionTouchEventX
        currentY = motionTouchEventY
        val id = UUIDService.generateUUID()
        var ellipseData = EllipseData(
            id = id,
            fill = "none",
            stroke = ColorService.getColorAsString(),
            fillOpacity = "1",
            strokeOpacity = "1",
            strokeWidth = EllipseService.currentWidth,
            x = currentX.toInt(),
            y = currentY.toInt(),
            width = 0,
            height = 0
        )

        var shapeDrawable: ShapeDrawable = ShapeDrawable(OvalShape())
        var layerIndex = CanvasService.layerDrawable.addLayer(shapeDrawable)
        ellipseCommand = EllipseCommand(ellipseData, layerIndex)
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