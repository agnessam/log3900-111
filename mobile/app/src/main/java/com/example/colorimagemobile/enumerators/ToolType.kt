package com.example.colorimagemobile.enumerators

import com.example.colorimagemobile.classes.commands.ColorPaletteCommand
import com.example.colorimagemobile.classes.commands.EraserCommand
import com.example.colorimagemobile.classes.commands.PencilCommand
import com.example.colorimagemobile.interfaces.IToolCommand

enum class ToolType(val title: String, val command: IToolCommand) {
    PENCIL("Pencil", PencilCommand()),
    ERASER("Eraser", EraserCommand()),
    COLOR_PALETTE("Color Palette", ColorPaletteCommand())
}