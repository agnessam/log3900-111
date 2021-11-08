package com.example.colorimagemobile.services.socket

import com.example.colorimagemobile.classes.AbsSocket
import com.example.colorimagemobile.interfaces.SocketTool
import com.example.colorimagemobile.utils.Constants.SOCKETS

object DrawingSocketService: AbsSocket(SOCKETS.COLLABORATIVE_DRAWING_NAMESPACE) {
    private var roomName: String? = null

    init {
        super.connect()
    }

    override fun joinRoom(roomName: String) {
        this.roomName = roomName
        super.joinRoom(roomName)
    }

    fun sendInProgressDrawingCommand(drawingCommand: Any, type: String) {
        val socketToolCommand = SocketTool(
            type = type,
            roomName = this.roomName as String,
            drawingCommand = drawingCommand
        )

        super.emit(SOCKETS.IN_PROGRESS_DRAWING_EVENT, socketToolCommand)
    }
}