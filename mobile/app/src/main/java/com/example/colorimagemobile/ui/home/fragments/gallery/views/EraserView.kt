package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import com.example.colorimagemobile.services.drawing.toolsAttribute.EraserService
import com.example.colorimagemobile.services.drawing.PathService
import kotlin.math.abs

class EraserView(context: Context?): CanvasView(context) {

    init {
//        paintPath.brush.setStrokeWidth(EraserService.getCurrentWidthAsFloat())
    }

    override fun createPathObject() {
    }

    override fun onTouchDown() { }

    override fun onTouchMove() {  }

    override fun onTouchUp() {
        val incertitudeMargin = 50
        val totalMargin = incertitudeMargin + EraserService.currentWidth

        PathService.getPaintPath().forEach {

            // iterate over each coordinates for each shape/path
            it.points.forEach { point ->
                val xDifference = abs(point.x.toInt() - motionTouchEventX.toInt()) <= totalMargin
                val yDifference = abs(point.y.toInt() - motionTouchEventY.toInt()) <= totalMargin

                // remove path from list
                if (xDifference && yDifference) {
                    PathService.removeByID(it.id)
//                    super.drawPreviousCanvas()
                    invalidate()
                    return
                }
            }
        }
    }
}