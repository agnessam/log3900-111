package com.example.colorimagemobile.services.drawing.toolsAttribute

import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import com.example.colorimagemobile.classes.toolsCommand.*
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.services.drawing.DrawingObjectManager

object LineWidthService: Attributes {
    override val minWidth = 1
    override val maxWidth = 50
    override var currentWidth: Int = 0

    private var lineWidthCommand: LineWidthCommand? = null

    fun changeLineWidth(newValue: Int) {
        lineWidthCommand = LineWidthCommand()
        if (SelectionService.selectedShapeIndex != -1) {
            val command = DrawingObjectManager.getCommand(SelectionService.selectedShapeIndex)
            when(command) {
                is PencilCommand -> {
                    command.pencil.strokeWidth = newValue
                    (DrawingObjectManager.getDrawable(SelectionService.selectedShapeIndex) as ShapeDrawable)
                        .paint.strokeWidth =
                        newValue.toFloat()
                }
                is RectangleCommand -> {
                    command.rectangle.strokeWidth = newValue
                    command.setEndPoint(command.endingPoint!!)
                    command.execute()
                }
                is EllipseCommand -> {
                    command.ellipse.strokeWidth = newValue
                    command.setEndPoint(command.endingPoint!!)
                    command.execute()
                }
            }
            // TODO: selection bounds for pencil
        }
        lineWidthCommand!!.execute()
    }
}