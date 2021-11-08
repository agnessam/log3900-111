package com.example.colorimagemobile.interfaces

import com.example.colorimagemobile.services.drawing.Point

data class ToolData(val id: String, val pointsList: ArrayList<Point>, val fill: String, val stroke: String, val fillOpacity: String, val strokeOpacity: String, val strokeWidth: String)
data class SocketTool(val type: String, val roomName: String, val drawingCommand: Any)

data class InProgressPencil(val id: String, var point: Point)