package com.example.colorimagemobile.classes.toolsCommand

import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.Point

class PencilCommand(paintPath: PaintPath): ICommand {
    private val pencilPaintPath: PaintPath = paintPath

    fun addPoint(point: Point) {
        pencilPaintPath.points.add(point)
    }

    // synchro
    override fun update(drawingCommand: Any) {
    }

    // update le canvas
    override fun execute() {
        CanvasService.extraCanvas.drawPath(pencilPaintPath.path, pencilPaintPath.brush.getPaint())
    }
}