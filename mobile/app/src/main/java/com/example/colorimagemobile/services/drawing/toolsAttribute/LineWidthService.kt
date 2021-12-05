package com.example.colorimagemobile.services.drawing.toolsAttribute

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.classes.toolsCommand.LineWidthCommand
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.socket.DrawingSocketService

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
        if (SelectionService.selectedShapeIndex != -1) {
            updateCurrentWidth(newValue)
            val id = DrawingObjectManager.getUuid(SelectionService.selectedShapeIndex)
            id ?: return
            lineWidthCommand = LineWidthCommand(id, newValue)
            lineWidthCommand!!.execute()
            SelectionService.resetBoundingBox()
            DrawingSocketService.sendLineWidthChange(id, newValue)
        }
    }
}