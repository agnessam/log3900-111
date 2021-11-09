package com.example.colorimagemobile.classes.toolsCommand

import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.interfaces.SyncUpdate
import com.example.colorimagemobile.services.drawing.*

class PencilCommand(paintPath: PaintPath): ICommand {
    private val pencilPaintPath: PaintPath = paintPath

    fun addPoint(point: Point) {
        pencilPaintPath.points.add(point)
        PathService.addPaintPath(pencilPaintPath)
    }

    // for synchro
    override fun update(drawingCommand: SyncUpdate) {
        pencilPaintPath.path.lineTo(drawingCommand.point.x, drawingCommand.point.y)
        addPoint(drawingCommand.point)
    }

    // update canvas
    override fun execute() {
        CanvasService.extraCanvas.drawPath(pencilPaintPath.path, pencilPaintPath.brush.getPaint())
        CanvasUpdateService.invalidate()
    }
}