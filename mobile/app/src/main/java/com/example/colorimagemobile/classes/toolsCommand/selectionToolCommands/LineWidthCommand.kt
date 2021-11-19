package com.example.colorimagemobile.classes.toolsCommand

import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.CanvasUpdateService

class LineWidthCommand: ICommand {
    override fun update(drawingCommand: Any) {
        TODO("Not yet implemented")
    }

    override fun execute() {
        CanvasUpdateService.invalidate()
    }

}