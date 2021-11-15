package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.hardware.usb.UsbEndpoint
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.RectangleData
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.SelectionService

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