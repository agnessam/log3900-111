package com.example.colorimagemobile.classes.toolsCommand

import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.toolsAttribute.LineWidthService
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService

class LineWidthCommand(objectId: Int, private var lineWidth: Int): ICommand {
    private var commandToChange: ICommand? = null

    init {
        commandToChange = DrawingObjectManager.getCommand(objectId)
    }

    override fun update(drawingCommand: Any) {
        TODO("Not yet implemented")
    }

    private fun PencilCommand.setLineWidth(newValue: Int) {
        pencil.strokeWidth = newValue
        initializePaint()
        execute()
    }

    private fun RectangleCommand.setLineWidth(newValue: Int) {
        rectangle.strokeWidth = newValue
        setEndPoint(endingPoint!!)
        execute()
    }

    private fun EllipseCommand.setLineWidth(newValue: Int) {
        ellipse.strokeWidth = newValue
        setEndPoint(endingPoint!!)
        execute()
    }

    override fun execute() {
        when(commandToChange){
            is PencilCommand -> (commandToChange as PencilCommand).setLineWidth(lineWidth)
            is RectangleCommand -> (commandToChange as RectangleCommand).setLineWidth(lineWidth)
            is EllipseCommand -> (commandToChange as EllipseCommand).setLineWidth(lineWidth)
            else -> {}
        }
        SelectionService.resetBoundingBox()
        CanvasUpdateService.invalidate()
    }

}