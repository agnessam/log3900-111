package com.example.colorimagemobile.classes

import com.example.colorimagemobile.classes.toolsCommand.EllipseCommand
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.classes.toolsCommand.RectangleCommand
import com.example.colorimagemobile.classes.toolsCommand.SelectionCommand
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.services.drawing.PaintPath

class CommandFactory {

    companion object {
        fun createCommand(commandType: String, toolData: Any): ICommand? {
            return when(commandType) {
                "Pencil" -> PencilCommand(toolData as PencilData)
                "Rectangle" -> RectangleCommand(toolData as RectangleData)
                "Ellipse" -> EllipseCommand(toolData as EllipseData)
                "SelectionStart" -> SelectionCommand(toolData as SelectionData)
                else -> null
            }
        }
    }
}