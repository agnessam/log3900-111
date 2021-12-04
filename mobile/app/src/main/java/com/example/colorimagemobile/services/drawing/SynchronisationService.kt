package com.example.colorimagemobile.services.drawing

import com.example.colorimagemobile.classes.CommandFactory
import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.classes.toolsCommand.selectionToolCommands.ResizeCommand
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import org.json.JSONObject
import java.net.Socket

object SynchronisationService {
    private var lastXTranslate: Float = 0f
    private var lastYTranslate: Float = 0f
    private val previewShapes: HashMap<String, ICommand> = HashMap()

    fun isShapeInPreview(uuid: String?): Boolean{
        return previewShapes[uuid] != null
    }

    fun removeFromPreview( toolCommandString: String): Boolean{
        val toolCommand = JSONObject(toolCommandString)
        val shapeId = JSONObject(toolCommand["drawingCommand"].toString())["id"]

        var removedCommand = previewShapes.remove(shapeId)
        return removedCommand != null
    }

    fun draw(socketToolArgs: String) {
        if(!CanvasService.extraCanvasIsIntialized()) return

        val drawingCommandJSON = JSONObject(socketToolArgs)

        val drawingCommand = JSONObject(drawingCommandJSON["drawingCommand"].toString())
        val commandId: String = drawingCommand["id"].toString()
        val toolType: String = drawingCommandJSON["type"] as String

        var command: ICommand?
        if(previewShapes.containsKey(commandId)){
            command = this.previewShapes[commandId]!!
            var updateData = when(toolType){
                "Pencil" -> {
                    var point = JSONObject(drawingCommand["point"].toString())
                    var x: Float = (point["x"] as Int).toFloat()
                    var y: Float = (point["y"] as Int).toFloat()
                    var newPoint = Point(x, y)
                    SyncUpdate(newPoint)
                }
                "Rectangle" -> {
                    var x: Float = when(drawingCommand["x"]){
                      is Double -> (drawingCommand["x"] as Double).toFloat()
                      is Int -> (drawingCommand["x"] as Int).toFloat()
                      else -> throw Exception("Rectangle x received isn't a number?")
                    }

                    var y: Float = when(drawingCommand["y"]){
                        is Double -> (drawingCommand["y"] as Double).toFloat()
                        is Int -> (drawingCommand["y"] as Int).toFloat()
                        else -> throw Exception("Rectangle y received isn't a number?")
                    }

                    var width: Float = (drawingCommand["width"] as Int).toFloat()
                    var height: Float = (drawingCommand["height"] as Int).toFloat()
                    RectangleUpdate(x, y, width, height)
                }
                "Ellipse" -> {
                    var x: Float = when(drawingCommand["x"]){
                        is Double -> (drawingCommand["x"] as Double).toFloat()
                        is Int -> (drawingCommand["x"] as Int).toFloat()
                        else -> throw Exception("Rectangle x received isn't a number?")
                    }

                    var y: Float = when(drawingCommand["y"]){
                        is Double -> (drawingCommand["y"] as Double).toFloat()
                        is Int -> (drawingCommand["y"] as Int).toFloat()
                        else -> throw Exception("Rectangle y received isn't a number?")
                    }

                    var width: Float = (drawingCommand["width"] as Int).toFloat()
                    var height: Float = (drawingCommand["height"] as Int).toFloat()
                    EllipseUpdate(x, y, width, height)
                }
                else -> null
            }

            if(updateData != null){
                command?.update(updateData)
            }
        } else{
            var toolData = this.getToolData(toolType, drawingCommandJSON)
            command = CommandFactory.createCommand(toolType, toolData)
        }
        if(command != null){
            previewShapes[commandId] = command
            command.execute()
        }
    }

    private fun getToolData(toolType: String, drawingCommandJSON: JSONObject): Any{
        val derivedToolClass = when (toolType) {
            "Pencil" -> PencilData::class.java
            "Rectangle" -> RectangleData::class.java
            "Ellipse" -> EllipseData::class.java
            "SelectionStart" -> SelectionData::class.java
            else -> throw Exception("Unrecognized tool type received in socket")
        }
        var toolDataString = drawingCommandJSON["drawingCommand"].toString()
        return JSONConvertor.getJSONObject(toolDataString, derivedToolClass)
    }

    fun startSelection(selectionCommandData: SocketTool) {
        val selectionCommand = CommandFactory.createCommand(
            selectionCommandData.type,
            selectionCommandData.drawingCommand
        )
        this.previewShapes[(selectionCommandData.drawingCommand as SelectionData).id] =
            selectionCommand!!
    }

    fun confirmSelection(confirmSelectionData: SocketTool) {
        this.previewShapes.remove((confirmSelectionData.drawingCommand as SelectionData).id)
    }

    fun transformSelection(transformSelectionData: SocketTool) {
        val commandId = when(transformSelectionData.type) {
            "SelectionResize" -> (transformSelectionData.drawingCommand as ResizeData).id
                "Translation" -> (transformSelectionData.drawingCommand as TranslateData).id
            else -> {""}
        }
        if(this.isShapeInPreview(commandId)){
            var command = previewShapes[commandId]
            var transformCommand = CommandFactory.createCommand(transformSelectionData.type, transformSelectionData.drawingCommand)

            if(transformCommand == null || command == null) return

            if(transformCommand.javaClass == command.javaClass){
                if(command is ResizeCommand){
                    if((transformSelectionData.drawingCommand as ResizeData).xTranslate != lastXTranslate || transformSelectionData.drawingCommand.yTranslate != lastYTranslate){
                        command.resetPathWithShapePath()
                    }

                    lastXTranslate = transformSelectionData.drawingCommand.xTranslate
                    lastYTranslate = transformSelectionData.drawingCommand.yTranslate
                }

                command.update(transformSelectionData.drawingCommand)

            }
            else{
                previewShapes[commandId] = transformCommand
            }
        }

        previewShapes[commandId]?.execute()
    }

    fun deleteSelection(deleteSelectionData: SocketTool){
        val deleteCommand = CommandFactory.createCommand(deleteSelectionData.type, deleteSelectionData.drawingCommand)
        deleteCommand?.execute()
    }

    fun setObjectPrimaryColor(colorData: ColorData) {
        val primaryColorCommand = CommandFactory.createCommand("PrimaryColor", colorData)
        primaryColorCommand?.execute()
    }

    fun setObjectSecondaryColor(colorData: ColorData) {
        val secondaryColorCommand = CommandFactory.createCommand("SecondaryColor", colorData)
        secondaryColorCommand?.execute()
    }

    fun setSelectionLineWidth(lineWidthData: LineWidthData) {
        val lineWidthCommand = CommandFactory.createCommand("LineWidth", lineWidthData)
        lineWidthCommand?.execute()
    }
}