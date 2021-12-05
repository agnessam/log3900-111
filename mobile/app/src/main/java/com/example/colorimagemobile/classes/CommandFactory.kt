package com.example.colorimagemobile.classes

import com.example.colorimagemobile.classes.toolsCommand.selectionToolCommands.ResizeCommand
import com.example.colorimagemobile.classes.toolsCommand.*
import com.example.colorimagemobile.classes.toolsCommand.selectionToolCommands.SelectionCommand
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService

class CommandFactory {

    companion object {
        var nonShapeCommands = arrayOf("SelectionStart", "SelectionResize", "Translation", "Delete", "PrimaryColor", "SecondaryColor", "LineWidth")

        fun createCommand(commandType: String, toolData: Any): ICommand? {
            if( commandType !in nonShapeCommands
                && (toolData as ToolData).stroke == null)
                return null

            when(commandType) {
                "Pencil" -> return PencilCommand(toolData as PencilData)
                "Rectangle" -> return RectangleCommand(toolData as RectangleData)
                "Ellipse" -> return EllipseCommand(toolData as EllipseData)
                "SelectionStart" -> return SelectionCommand(toolData as SelectionData)
                "SelectionResize" -> {
                    var resizeCommand = ResizeCommand((toolData as ResizeData).id)
                    resizeCommand.setScales(toolData.xScaled, toolData.yScaled, toolData.xTranslate, toolData.yTranslate)
                    return resizeCommand
                }
                "Translation" -> {
                    val translateCommand = TranslateCommand(toolData as TranslateData)
                    translateCommand.setTransformation(toolData.deltaX, toolData.deltaY)
                    return translateCommand
                }
                "Delete" -> {
                    return DeleteCommand((toolData as DeleteData).id)
                }
                "PrimaryColor" -> {
                    val newColor = ColorService.convertRGBAndOpacityToRGBAString((toolData as ColorData).color, toolData.opacity)
                    return PrimaryColorCommand(toolData.id, newColor)
                }
                "SecondaryColor" -> {
                    val newColor = ColorService.convertRGBAndOpacityToRGBAString((toolData as ColorData).color, toolData.opacity)
                    return SecondaryColorCommand(toolData.id, newColor)
                }
                "LineWidth" -> {
                    return LineWidthCommand((toolData as LineWidthData).id, toolData.lineWidth)
                }
            }
            return null
        }
    }
}