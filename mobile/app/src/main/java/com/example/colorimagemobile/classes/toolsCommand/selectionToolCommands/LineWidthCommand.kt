package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Path
import android.graphics.RectF
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingJsonService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService

class LineWidthCommand(objectId: String, private var lineWidth: Int): ICommand {
    private var commandToChange: ICommand? = null
    private var resizeFillPath: Path? = null
    private var resizeBorderPath: Path? = null

    init {
        commandToChange = DrawingObjectManager.getCommand(objectId)
        resetPathWithShapePath()
    }

    override fun update(drawingCommand: Any) {
        TODO("Not yet implemented")
    }

    private fun PencilCommand.setLineWidth(newValue: Int) {
        pencil.strokeWidth = newValue
        DrawingJsonService.updatePolylineWidth(pencil)
        initializePaint()
        execute()
    }

    private fun RectangleCommand.setLineWidth(newValue: Int) {
        rectangle.strokeWidth = newValue
        DrawingJsonService.updateRectangleWidth(rectangle)

        var fillBounds = RectF()
        resizeBorderPath!!.computeBounds(fillBounds, true)
        traceFillPath(fillBounds.left, fillBounds.top, fillBounds.right, fillBounds.bottom)

        var borderBounds = RectF()
        resizeBorderPath!!.computeBounds(borderBounds, true)
        traceBorderPath(borderBounds.left, borderBounds.top, borderBounds.right, borderBounds.bottom)
        execute()
    }

    private fun EllipseCommand.setLineWidth(newValue: Int) {
        ellipse.strokeWidth = newValue
        DrawingJsonService.updateEllipseWidth(ellipse)

//        setEndPoint(endingPoint!!)
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

    private fun resetPathWithShapePath() {
        when(commandToChange) {
            is PencilCommand -> (commandToChange as PencilCommand).getPath()
            is EllipseCommand -> (commandToChange as EllipseCommand).getPaths()
            is RectangleCommand -> (commandToChange as RectangleCommand).getPaths()
            else -> null
        }
    }

    private fun PencilCommand.getPath(){
        resizeBorderPath = Path(path)
    }

    private fun EllipseCommand.getPaths(){
        resizeFillPath = Path(fillPath)
        resizeBorderPath = Path(borderPath)
    }

    private fun RectangleCommand.getPaths(){
        resizeFillPath = Path(fillPath)
        resizeBorderPath = Path(borderPath)
    }
}