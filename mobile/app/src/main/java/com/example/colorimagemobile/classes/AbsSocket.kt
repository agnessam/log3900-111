package com.example.colorimagemobile.classes

import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.services.socket.SocketHandler
import com.example.colorimagemobile.utils.Constants.SOCKETS
import io.socket.client.Socket

abstract class AbsSocket(namespace: String) {
    protected var mSocket: Socket

    init {
        SocketHandler.setSocket(namespace)
        mSocket = SocketHandler.getSocket()
    }

    fun connect() {
        mSocket.connect()
    }

    open fun disconnect() {
        mSocket.disconnect()
    }

    open fun joinRoom(roomName: String) {
        emit(SOCKETS.ROOM_EVENT_NAME, roomName)
    }

    fun leaveRoom(roomName: String) {
        emit(SOCKETS.LEAVE_ROOM_EVENT_NAME, roomName)
    }

    protected fun emit(event: String, data: Any) {
        mSocket.emit(event, data)
    }

    // MUST set current Activity/Fragment before listening to Sockets cuz of UI Thread
    abstract fun setFragmentActivity(fragmentAct: FragmentActivity)

    protected abstract fun setSocketListeners()
}