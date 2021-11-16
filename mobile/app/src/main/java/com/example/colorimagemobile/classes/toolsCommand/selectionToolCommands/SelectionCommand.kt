package com.example.colorimagemobile.classes.toolsCommand

import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService

class SelectionCommand(): ICommand {
    init {
        SelectionService.initSelectionRectangle()
    }

    override fun update(drawingCommand: Any) {
        TODO("Not yet implemented")
    }

    override fun execute() {
        CanvasUpdateService.invalidate()
    }
}