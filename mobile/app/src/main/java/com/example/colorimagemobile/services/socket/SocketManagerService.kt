package com.example.colorimagemobile.services.socket

import com.example.colorimagemobile.services.drawing.DrawingService

object SocketManagerService {

    // call this to disconnect from every socket
    fun disconnectFromAll() {
        DrawingSocketService.disconnect()
        ChatSocketService.disconnect()
    }

    fun leaveDrawingRoom() {
        if (DrawingService.getCurrentDrawingID() == null) return

        DrawingSocketService.leaveRoom(DrawingService.getCurrentDrawingID()!!)
        DrawingSocketService.disconnect()
    }
}