package com.example.colorimagemobile.classes

import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.classes.toolsCommand.RectangleCommand
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.PencilData
import com.example.colorimagemobile.models.RectangleData
import com.example.colorimagemobile.models.ToolData
import com.example.colorimagemobile.services.drawing.PaintPath

class CommandFactory {

    companion object {
        fun createCommand(commandType: String, toolData: ToolData): ICommand? {
            return when(commandType) {
                "Pencil" -> PencilCommand(toolData as PencilData)
                "Rectangle" -> RectangleCommand(toolData as RectangleData)
                else -> null
            }
        }
    }
}