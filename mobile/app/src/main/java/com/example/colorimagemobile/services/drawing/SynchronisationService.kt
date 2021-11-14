package com.example.colorimagemobile.services.drawing

import android.graphics.Path
import com.example.colorimagemobile.classes.CommandFactory
import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
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
            var updateData = when(toolType){
                "Pencil" -> {
                    var point = JSONObject(drawingCommand["point"].toString())
                    var x: Float = (point["x"] as Int).toFloat()
                    var y: Float = (point["y"] as Int).toFloat()
                    var newPoint = Point(x, y)
                    SyncUpdate(newPoint)
                }
                "Rectangle" -> {
                    var x: Int = when(drawingCommand["x"]){
                      is Double -> (drawingCommand["x"] as Double).toInt()
                      is Int -> drawingCommand["x"] as Int
                      else -> throw Exception("Rectangle x received isn't a number?")
                    }

                    var y: Int = when(drawingCommand["y"]){
                        is Double -> (drawingCommand["y"] as Double).toInt()
                        is Int -> drawingCommand["y"] as Int
                        else -> throw Exception("Rectangle y received isn't a number?")
                    }

                    var width: Int = drawingCommand["width"] as Int
                    var height: Int = drawingCommand["height"] as Int
                    RectangleUpdate(x, y, width, height)
                }
                else -> null
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