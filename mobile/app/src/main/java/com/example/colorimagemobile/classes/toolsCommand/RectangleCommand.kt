package com.example.colorimagemobile.classes.toolsCommand

import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.Point

class RectangleCommand(paintPath: PaintPath): ICommand {
    private val rectPaintPath: PaintPath = paintPath
    private var startingPoint: Point? = null
    private var endingPoint: Point? = null

    fun setStartPoint(startPoint: Point) {
        startingPoint = startPoint
    }

    fun setEndPoint(endPoint: Point) {
        endingPoint = endPoint
    }

    override fun update(drawingCommand: SyncUpdate) {
        TODO("Not yet implemented")
    }

    override fun execute() {
        CanvasService.extraCanvas.drawRect(startingPoint!!.x, startingPoint!!.y, endingPoint!!.x, endingPoint!!.y, rectPaintPath.brush.getPaint())
        CanvasUpdateService.invalidate()
    }
}