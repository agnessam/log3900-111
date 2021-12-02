package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingJsonService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService

class DeleteCommand(objectId: String): ICommand {
    private var id: String = objectId
    private var deleteShapeLayerIndex: Int = DrawingObjectManager.getLayerIndex(id)
    var deletedShape: Drawable? = DrawingObjectManager.getDrawable(this.deleteShapeLayerIndex)
    override fun update(drawingCommand: Any) {
        // No update for deleteCommand
    }

    private fun deleteFromJson() {

        when (DrawingObjectManager.getCommand(SelectionService.selectedShapeIndex)) {
            is PencilCommand -> DrawingJsonService.removePolyline(id)
            is RectangleCommand -> DrawingJsonService.removeRectangle(id)
            is EllipseCommand -> DrawingJsonService.removeEllipse(id)
        }
    }

    override fun execute() {
        var emptyShapeDrawable = ShapeDrawable()
        emptyShapeDrawable.paint.alpha = 0

        deleteFromJson()
        DrawingObjectManager.setDrawable(deleteShapeLayerIndex, emptyShapeDrawable)
        DrawingObjectManager.removeCommand(deleteShapeLayerIndex)
        CanvasUpdateService.invalidate()
    }
}