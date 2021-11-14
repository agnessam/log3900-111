package com.example.colorimagemobile.services.drawing

import android.graphics.Path
import com.example.colorimagemobile.classes.CommandFactory
import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import org.json.JSONObject

object SynchronisationService {

    private val previewShapes: HashMap<String, ICommand> = HashMap()

    fun draw(socketToolArgs: String) {

        val drawingCommandJSON = JSONObject(socketToolArgs)

        val drawingCommand = JSONObject(drawingCommandJSON["drawingCommand"].toString())
        val commandId: String = drawingCommand["id"].toString()
        val toolType: String = drawingCommandJSON["type"] as String

        var command: ICommand?
        if(previewShapes.containsKey(commandId)){
            command = this.previewShapes[commandId]!!
            var updateData = if(toolType == "Pencil"){
                var point = JSONObject(drawingCommand["point"].toString())
                var x: Float = (point["x"] as Int).toFloat()
                var y: Float = (point["y"] as Int).toFloat()
                var newPoint = Point(x, y)
                SyncUpdate(newPoint)
            }
            else{
                null
            }
//            var toolData =
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

    private fun getToolData(toolType: String, drawingCommandJSON: JSONObject): ToolData{
        val derivedToolClass = when (toolType) {
            "Pencil" -> PencilData::class.java
            "Rectangle" -> RectangleData::class.java
            else -> throw Exception("Unrecognized tool type received in socket")
        }
        var toolDataString = drawingCommandJSON["drawingCommand"].toString()
        return JSONConvertor.getJSONObject(toolDataString, derivedToolClass)
    }
}