package com.example.colorimagemobile.services.drawing.toolsAttribute

import android.graphics.drawable.ShapeDrawable
import com.example.colorimagemobile.classes.toolsCommand.DeleteCommand
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.socket.DrawingSocketService

object DeleteService {
    var deleteCommand: DeleteCommand? = null

    fun deleteSelectedLayer() {
        val selectedObjectId = DrawingObjectManager.getUuid(SelectionService.selectedShapeIndex)
        if(selectedObjectId != null) {
            deleteCommand = DeleteCommand(selectedObjectId)
            if(SelectionService.isShapeInitialized() && deleteCommand!!.deletedShape == SelectionService.selectedShape){
                deleteCommand!!.execute()
                DrawingSocketService.sendDeleteSelectionCommand(selectedObjectId) // TODO("DELETE OBJECT WITH SOCKET")
                SelectionService.selectedShapeIndex = -1
                SelectionService.selectedShape = ShapeDrawable()
                SelectionService.clearSelection()
            }
        }
    }
}