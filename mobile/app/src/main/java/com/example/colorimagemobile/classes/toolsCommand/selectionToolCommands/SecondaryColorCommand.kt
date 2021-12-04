package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Color
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingJsonService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.ShapeLabel
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService

class SecondaryColorCommand(private val objectId: String, private var secondaryColor: String): ICommand {
    private var commandToChange: ICommand? = null

    init {
        commandToChange = DrawingObjectManager.getCommand(objectId)
    }

    override fun update(drawingCommand: Any) {
        TODO("Not yet implemented")
    }

    fun getSecondaryColor(): String {
        return when(commandToChange){
            is RectangleCommand -> (commandToChange as RectangleCommand).getSecondaryColor()
            is EllipseCommand -> (commandToChange as EllipseCommand).getSecondaryColor()
            else -> ColorService.intToRGBA(Color.TRANSPARENT)
        }
    }

    private fun RectangleCommand.getSecondaryColor(): String{
        if(rectangle.stroke == "none") return "none"
        val colorInt = ColorService.rgbaToInt(rectangle.stroke)
        return ColorService.intToRGBA(colorInt)
    }

    private fun EllipseCommand.getSecondaryColor(): String{
        if(ellipse.stroke == "none") return "none"
        val colorInt = ColorService.rgbaToInt(ellipse.stroke)
        return ColorService.intToRGBA(colorInt)
    }

    private fun RectangleCommand.setSecondaryColor(newColor: String) {
        if(rectangle.stroke != "none"){
            rectangle.stroke = newColor
            rectangle.strokeOpacity = ColorService.getAlphaForDesktop(newColor)
            DrawingJsonService.updateShapeStrokeColor(newColor, rectangle.strokeOpacity, objectId, ShapeLabel.RECTANGLE)
            borderPaint = initializePaint(newColor, rectangle.strokeOpacity, Color.WHITE)
            execute()
        }
    }

    private fun EllipseCommand.setSecondaryColor(newColor: String) {
        if(ellipse.stroke != "none"){
            ellipse.stroke = newColor
            ellipse.strokeOpacity = ColorService.getAlphaForDesktop(newColor)
            DrawingJsonService.updateShapeStrokeColor(newColor, ellipse.strokeOpacity, objectId, ShapeLabel.ELLIPSE)
            borderPaint = initializePaint(newColor, ellipse.strokeOpacity, Color.WHITE)
            execute()
        }
    }

    override fun execute() {
        when(commandToChange){
            is RectangleCommand -> (commandToChange as RectangleCommand).setSecondaryColor(secondaryColor)
            is EllipseCommand -> (commandToChange as EllipseCommand).setSecondaryColor(secondaryColor)
            else -> {}
        }
        CanvasUpdateService.invalidate()
    }
}