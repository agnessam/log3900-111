package com.example.colorimagemobile.classes

import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.PaintPath

class CommandFactory {

    companion object {
        fun createCommand(commandType: String, paintPath: PaintPath): ICommand? {
            return when(commandType) {
                "Pencil" -> PencilCommand(paintPath)
                else -> null
            }
        }
    }
}