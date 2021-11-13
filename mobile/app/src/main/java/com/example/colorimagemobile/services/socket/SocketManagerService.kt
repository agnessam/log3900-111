package com.example.colorimagemobile.services.socket

object SocketManagerService {

    // call this to disconnect from every socket
    fun disconnectFromAll() {
        DrawingSocketService.disconnect()
    }
}