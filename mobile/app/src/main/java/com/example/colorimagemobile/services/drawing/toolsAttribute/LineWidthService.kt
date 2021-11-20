package com.example.colorimagemobile.services.drawing.toolsAttribute

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.classes.toolsCommand.EllipseCommand
import com.example.colorimagemobile.classes.toolsCommand.LineWidthCommand
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.classes.toolsCommand.RectangleCommand
import com.example.colorimagemobile.services.drawing.DrawingObjectManager

object LineWidthService: Attributes {
    override val minWidth = 1
    override val maxWidth = 50
    override var currentWidth: Int = 0

    private val updateCurrentWidth: MutableLiveData<Int> = MutableLiveData()

    private var lineWidthCommand: LineWidthCommand? = null

    fun updateCurrentWidth(newValue: Int) {
        updateCurrentWidth.value = newValue
    }

    fun getCurrentWidth() : LiveData<Int> {
        return updateCurrentWidth
    }

    fun changeLineWidth(newValue: Int) {
        lineWidthCommand = LineWidthCommand()
        if (SelectionService.selectedShapeIndex != -1) {
            val command = DrawingObjectManager.getCommand(SelectionService.selectedShapeIndex)
            when(command) {
                is PencilCommand -> {
                    updateCurrentWidth.value = newValue
                    command.pencil.strokeWidth = newValue
                    command.initializePaint()
                    command.execute()
                }
                is RectangleCommand -> {
                    updateCurrentWidth.value = newValue
                    command.rectangle.strokeWidth = newValue
                    command.setEndPoint(command.endingPoint!!)
                    command.execute()
                }
                is EllipseCommand -> {
                    updateCurrentWidth.value = newValue
                    command.ellipse.strokeWidth = newValue
                    command.setEndPoint(command.endingPoint!!)
                    command.execute()
                }
            }
            // TODO: selection bounds for pencil
            SelectionService.resetBoundingBox()
        }
        lineWidthCommand!!.execute()
    }
}