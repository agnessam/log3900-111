package com.example.colorimagemobile.classes.toolsCommand

import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.interfaces.SyncUpdate
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

class PencilCommand(paintPath: PaintPath): ICommand {
    private val pencilPaintPath: PaintPath = paintPath

    fun addPoint(point: Point) {
        pencilPaintPath.points.add(point)
    }

    // for synchro
    override fun update(drawingCommand: SyncUpdate) {
        addPoint(drawingCommand.point)
    }

    // update canvas
    override fun execute() {
        printMsg("PencilCommand: ${pencilPaintPath.toString()}")
        CanvasService.extraCanvas.drawPath(pencilPaintPath.path, pencilPaintPath.brush.getPaint())
//        CanvasService.extraCanvas.
    }
}