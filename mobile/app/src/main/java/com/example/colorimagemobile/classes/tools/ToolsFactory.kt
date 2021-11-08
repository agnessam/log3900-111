package com.example.colorimagemobile.classes.tools

import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.interfaces.ITool

class ToolsFactory {
    private var tool: ToolType = ToolType.PENCIL

    fun getTool(toolType: ToolType): ITool {
        this.tool = toolType

        return when(this.tool) {
            ToolType.PENCIL -> PencilTool()
            ToolType.ERASER -> EraserTool()
            ToolType.COLOR_PALETTE -> ColorPaletteTool()
            ToolType.RECTANGLE ->RectangleTool()
        }
    }
}