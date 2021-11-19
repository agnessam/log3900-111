package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.hardware.usb.UsbEndpoint
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.SelectionService

class SelectionCommand(var selectionData: SelectionData): ICommand {

    init {
        SelectionService.initSelectionRectangle()
    }

    override fun update(drawingCommand: Any) {
        if(drawingCommand is SelectionUpdate) {
            // uhhhhhhhhhhhhhhhhhhhhhhhh
        }
    }

    override fun execute() {
    }
}