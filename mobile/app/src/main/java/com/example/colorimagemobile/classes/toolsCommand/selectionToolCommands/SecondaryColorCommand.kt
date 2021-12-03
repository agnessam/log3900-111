package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Color
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService

class SecondaryColorCommand(private val objectId: String, private var secondaryColor: String): ICommand {
    private var commandToChange: ICommand? = null

    init {
        commandToChange = DrawingObjectManager.getCommand(objectId)
    }

    override fun update(drawingCommand: Any) {
        TODO("Not yet implemented")
    }

    private fun RectangleCommand.setSecondaryColor(newColor: String) {
        rectangle.stroke = newColor
        rectangle.strokeOpacity = ColorService.getAlphaForDesktop(newColor)
        borderPaint = initializePaint(newColor, rectangle.strokeOpacity, Color.WHITE)
        execute()
    }

    private fun EllipseCommand.setSecondaryColor(newColor: String) {
        ellipse.stroke = newColor
        ellipse.strokeOpacity = ColorService.getAlphaForDesktop(newColor)
        borderPaint = initializePaint(newColor, ellipse.strokeOpacity, Color.WHITE)
        execute()
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