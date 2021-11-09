package com.example.colorimagemobile.interfaces

import com.example.colorimagemobile.services.drawing.Point

data class ToolData(val id: String, val pointsList: ArrayList<Point>, val fill: String, val stroke: String, val fillOpacity: String, val strokeOpacity: String, val strokeWidth: Int)
data class SocketTool(val type: String, val roomName: String, val drawingCommand: Any)

data class InProgressPencil(val id: String, var point: Point)

// object used during synchronisation
data class SyncCreateDrawing(val type: String, val roomName: String, val drawingCommand: ToolData)

data class SyncUpdate(val id: String, var point: Point)
data class SyncUpdateDrawing(val type: String, val roomName: String, val drawingCommand: SyncUpdate)