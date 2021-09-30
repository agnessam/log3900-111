package com.example.colorimagemobile

import android.util.Log
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
    private const val SERVER_URL = "http://10.0.2.2:3000"
    private lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket(SERVER_URL)
            Log.d("setting socket good","setting socket done")
        } catch (e: URISyntaxException) {
            Log.d("setting socket error","Error setting up socket $e")
            throw  RuntimeException(e) // by beatrice
        }
    }
/*
    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }*/

    @Synchronized
    fun getSocket(): Socket {
        Log.d("get socket","get socket function")
        return mSocket

    }

    @Synchronized
    fun establishConnection() {
        Log.d("establish connexion","connect socket function")
        mSocket.connect()
    }
/*
    @Synchronized
    fun closeConnection() {
        print("disconnected socket")
        mSocket.disconnect()
    }
   */

}