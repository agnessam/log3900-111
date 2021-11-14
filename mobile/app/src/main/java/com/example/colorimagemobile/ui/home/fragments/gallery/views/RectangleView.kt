package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.classes.toolsCommand.RectangleCommand
import com.example.colorimagemobile.models.RectangleData
import com.example.colorimagemobile.services.UUIDService
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CustomPaint
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import com.example.colorimagemobile.services.drawing.toolsAttribute.RectangleService
import com.example.colorimagemobile.ui.home.fragments.gallery.views.CanvasView
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import kotlin.math.abs

class RectangleView(context: Context?): CanvasView(context) {
    private var paintPath: PaintPath? = null
    private var rectangleCommand: RectangleCommand? = null
    private val rectangleType = "Rectangle"

    override fun createPathObject() {
        currentX = motionTouchEventX
        currentY = motionTouchEventY
        val id = UUIDService.generateUUID()
        var rectangleData = RectangleData(
            id = id,
            fill = "none",
            stroke = ColorService.getColorAsString(),
            fillOpacity = "1",
            strokeOpacity = "1",
            strokeWidth = RectangleService.currentWidth,
            x = currentX.toInt(),
            y = currentY.toInt(),
            width = 0,
            height = 0
        )

        var shapeDrawable: ShapeDrawable = ShapeDrawable(RectShape())
        var layerIndex = CanvasService.layerDrawable.addLayer(shapeDrawable)
        rectangleCommand = RectangleCommand(rectangleData, layerIndex)
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