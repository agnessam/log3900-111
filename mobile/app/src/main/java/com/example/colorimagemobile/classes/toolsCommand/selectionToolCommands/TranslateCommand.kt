package com.example.colorimagemobile.classes.toolsCommand

import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.CanvasUpdateService

class TranslateCommand: ICommand {
    private var deltaX : Int = 0
    private var deltaY: Int = 0

    override fun update(drawingCommand: Any) {
//        setTransformation(drawingCommand.deltaX, drawingCommand.deltaY)
    }

    fun setTransformation(x: Int, y: Int) {
        this.deltaX = x
        this.deltaY = y
    }

    override fun execute() {
        CanvasUpdateService.invalidate()
    }
}