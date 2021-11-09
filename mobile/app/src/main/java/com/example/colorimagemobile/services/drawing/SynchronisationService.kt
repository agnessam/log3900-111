package com.example.colorimagemobile.services.drawing

import android.graphics.Path
import com.example.colorimagemobile.classes.CommandFactory
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.interfaces.SyncCreateDrawing
import com.example.colorimagemobile.interfaces.SyncUpdateDrawing
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

object SynchronisationService {

    private val previewShapes: HashMap<String, ICommand> = HashMap()

    fun createCommand(drawingCommand: SyncCreateDrawing) {
        val commandId = drawingCommand.drawingCommand.id
        val paintPath = PaintPath(commandId, CustomPaint(), Path(), arrayListOf())
        paintPath.points.add(drawingCommand.drawingCommand.pointsList[0])

        // set paint brush ..
//        paintPath.brush.getPaint().color = ColorService.convertColorToInt(drawingCommand.drawingCommand.stroke)
        paintPath.brush.getPaint().strokeWidth = drawingCommand.drawingCommand.strokeWidth.toFloat()

        val command: ICommand? = CommandFactory.createCommand(drawingCommand.type, paintPath)

        if (command != null) {
            command.execute()
            previewShapes[commandId] = command
        }
    }

    fun drawAndUpdate(drawingCommand: SyncUpdateDrawing) {
        val commandId = drawingCommand.drawingCommand.id
        val command = previewShapes[commandId]

        if (!previewShapes.containsKey(commandId) || command == null) { return }

        printMsg("Sync: $command")

        command.update(drawingCommand.drawingCommand)
        command.execute()
    }
}