package com.example.colorimagemobile.services

import com.example.colorimagemobile.utils.Constants.Companion.CHAT_NAMESPACE_NAME
import com.example.colorimagemobile.utils.Constants.Companion.SERVER_URL
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {
    private lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket("$SERVER_URL/$CHAT_NAMESPACE_NAME")
        } catch (e: URISyntaxException) {
            print("Error setting up socket $e")
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }
}