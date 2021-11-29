package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Color
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingJsonService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService

class PrimaryColorCommand(objectId: Int, private var primaryColor: String, private var opacity: String): ICommand {
    private var commandToChange: ICommand? = null

    init {
        commandToChange = DrawingObjectManager.getCommand(objectId)
    }

    override fun update(drawingCommand: Any) {
        TODO("Not yet implemented")
    }

    private fun PencilCommand.setColor(newColor: String, opacity: String) {
        pencil.stroke = newColor
        // intialize paint in pencil command doesn't seem to use stroke opacity?
        pencil.strokeOpacity = opacity
        initializePaint()
        execute()
    }

    private fun RectangleCommand.setColor(newColor: String, opacity: String) {
        rectangle.fill = newColor
        rectangle.fillOpacity = opacity
        initializePaint(newColor, opacity, Color.WHITE)
        execute()
    }

    private fun EllipseCommand.setColor(newColor: String, opacity: String) {
        ellipse.fill = newColor
        ellipse.fillOpacity = opacity
        initializePaint(newColor, opacity, Color.BLACK)
        execute()
    }

    override fun execute() {
        when(commandToChange){
            is PencilCommand -> (commandToChange as PencilCommand).setColor(primaryColor, opacity)
            is RectangleCommand -> (commandToChange as RectangleCommand).setColor(primaryColor, opacity)
            is EllipseCommand -> (commandToChange as EllipseCommand).setColor(primaryColor, opacity)
            else -> {}
        }
        CanvasUpdateService.invalidate()
    }
}