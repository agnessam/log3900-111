package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Color
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingJsonService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.ShapeLabel
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService

class PrimaryColorCommand(private val objectId: String, private var primaryColor: String): ICommand {
    private var commandToChange: ICommand? = null
    init {
        commandToChange = DrawingObjectManager.getCommand(objectId)
    }

    override fun update(drawingCommand: Any) {
        TODO("Not yet implemented")
    }

    private fun PencilCommand.setPrimaryColor(newColor: String) {
        pencil.stroke = newColor
        pencil.strokeOpacity = ColorService.getAlphaForDesktop(newColor)
        DrawingJsonService.updateShapeStrokeColor(newColor, pencil.strokeOpacity, objectId, ShapeLabel.POLYLINE)
        initializePaint()
        execute()
    }

    private fun RectangleCommand.setPrimaryColor(newColor: String) {
        rectangle.fill = newColor
        rectangle.fillOpacity = ColorService.getAlphaForDesktop(newColor)
        fillPaint = initializePaint(newColor, rectangle.fillOpacity, Color.BLACK)
        DrawingJsonService.updateShapeFillColor(newColor, rectangle.fillOpacity, objectId, ShapeLabel.RECTANGLE)
        execute()
    }

    private fun EllipseCommand.setPrimaryColor(newColor: String) {
        ellipse.fill = newColor
        ellipse.fillOpacity = ColorService.getAlphaForDesktop(newColor)
        fillPaint = initializePaint(newColor, ellipse.fillOpacity, Color.BLACK)
        DrawingJsonService.updateShapeFillColor(newColor, ellipse.fillOpacity, objectId, ShapeLabel.ELLIPSE)
        execute()
    }

    override fun execute() {
        when(commandToChange){
            is PencilCommand -> (commandToChange as PencilCommand).setPrimaryColor(primaryColor)
            is RectangleCommand -> (commandToChange as RectangleCommand).setPrimaryColor(primaryColor)
            is EllipseCommand -> (commandToChange as EllipseCommand).setPrimaryColor(primaryColor)
            else -> {}
        }
        CanvasUpdateService.invalidate()
    }
}