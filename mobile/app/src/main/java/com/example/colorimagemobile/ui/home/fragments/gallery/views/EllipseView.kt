package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import com.example.colorimagemobile.classes.toolsCommand.EllipseCommand
import com.example.colorimagemobile.classes.toolsCommand.RectangleCommand
import com.example.colorimagemobile.services.UUIDService
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.services.drawing.toolsAttribute.EllipseService
import com.example.colorimagemobile.services.drawing.toolsAttribute.RectangleService
import kotlin.math.abs

class EllipseView(context: Context?): CanvasView(context) {
    private var paintPath: PaintPath? = null
    private var ellipseCommand: EllipseCommand? = null

    override fun createPathObject() {
        var color = ColorService.getColorAsInt()
        var borderWidth = EllipseService.getCurrentWidthAsFloat()
        var commandId = UUIDService.generateUUID()

        var shapeDrawable: ShapeDrawable = ShapeDrawable(OvalShape())
        var layerIndex = CanvasService.layerDrawable.addLayer(shapeDrawable)
        ellipseCommand = EllipseCommand(layerIndex)
    }

    override fun onTouchDown() {
        CanvasService.extraCanvas.save()
        createPathObject()

        currentX = motionTouchEventX
        currentY = motionTouchEventY
        ellipseCommand!!.setStartPoint(Point(currentX, currentY))
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
        paintPath = null
    }
}