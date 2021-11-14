package com.example.colorimagemobile.services.drawing

import android.graphics.Path
import com.example.colorimagemobile.classes.CommandFactory
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.SyncCreateDrawing
import com.example.colorimagemobile.models.SyncUpdateDrawing
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService

object SynchronisationService {

    private val previewShapes: HashMap<String, ICommand> = HashMap()

    fun createCommand(drawingCommand: SyncCreateDrawing) {
        val commandId = drawingCommand.drawingCommand.id
        val paintPath = PaintPath(commandId, CustomPaint(), Path(), arrayListOf())
//        paintPath.points.add(drawingCommand.drawingCommand.pointsList[0])

        // set paint brush and stuff
        paintPath.brush.setColor(ColorService.rgbaToInt(drawingCommand.drawingCommand.stroke))
        paintPath.brush.setStrokeWidth(drawingCommand.drawingCommand.strokeWidth.toFloat())
        paintPath.path.moveTo(paintPath.points[0].x, paintPath.points[0].y)

//        val command: ICommand? = CommandFactory.createCommand(drawingCommand.type, paintPath)

//        if (command != null) {
//            command.execute()
//            previewShapes[commandId] = command
//        }
    }

    fun drawAndUpdate(drawingCommand: SyncUpdateDrawing) {
        val commandId = drawingCommand.drawingCommand.id
        val command = previewShapes[commandId]

        if (!previewShapes.containsKey(commandId) || command == null) { return }

        command.update(drawingCommand.drawingCommand)
        command.execute()
    }
}