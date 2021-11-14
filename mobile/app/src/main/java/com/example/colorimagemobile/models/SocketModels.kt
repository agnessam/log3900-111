package com.example.colorimagemobile.models

import com.example.colorimagemobile.services.drawing.Point

interface ToolData{
    var id: String
    var fill: String
    var stroke: String
    var fillOpacity: String
    var strokeOpacity: String
    var strokeWidth: Int
}

class PencilData(
    override var id: String,
    override var fill: String,
    override var stroke: String,
    override var fillOpacity: String,
    override var strokeOpacity: String,
    override var strokeWidth: Int,
    var pointsList: ArrayList<Point>,
    ): ToolData

class RectangleData(
    override var id: String,
    override var fill: String,
    override var stroke: String,
    override var fillOpacity: String,
    override var strokeOpacity: String,
    override var strokeWidth: Int,
    var x: Int,
    var y: Int,
    var width: Int,
    var height: Int
): ToolData

class EllipseData(
    override var id: String,
    override var fill: String,
    override var stroke: String,
    override var fillOpacity: String,
    override var strokeOpacity: String,
    override var strokeWidth: Int,
    var x: Int,
    var y: Int,
    var width: Int,
    var height: Int
): ToolData

data class SocketTool(val type: String, val roomName: String, val drawingCommand: Any)

data class InProgressPencil(val id: String, var point: Point)

// object used during synchronisation
data class SyncCreateDrawing(val type: String, val roomName: String, val drawingCommand: ToolData)

data class SyncUpdate(var point: Point)
data class RectangleUpdate(var x:Int, var y:Int, var width: Int, var height: Int)
data class SyncUpdateDrawing(val type: String, val roomName: String, val drawingCommand: SyncUpdate)