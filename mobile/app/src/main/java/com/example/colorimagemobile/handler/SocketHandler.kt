package com.example.colorimagemobile.handler

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

/*
* In Activity, import Socket:
        import io.socket.client.Socket

* In Activity: declare instance via:
        private lateinit var mSocket: Socket

* In Activity's constructor, set init socket:
        SocketHandler.setSocket()
        mSocket = SocketHandler.getSocket()
        mSocket.connect()

* And emit/listen

* */

object SocketHandler {
    private const val SERVER_URL = "https://"
    private lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket(SERVER_URL)
        } catch (e: URISyntaxException) {
            print("Error setting up socket $e")
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }
}