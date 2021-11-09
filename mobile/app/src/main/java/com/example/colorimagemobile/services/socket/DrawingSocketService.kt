package com.example.colorimagemobile.services.socket

import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.classes.AbsSocket
import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.models.SocketTool
import com.example.colorimagemobile.models.SyncCreateDrawing
import com.example.colorimagemobile.models.SyncUpdateDrawing
import com.example.colorimagemobile.services.drawing.SynchronisationService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants.SOCKETS
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.IN_PROGRESS_DRAWING_EVENT
import io.socket.emitter.Emitter
import org.json.JSONException

object DrawingSocketService: AbsSocket(SOCKETS.COLLABORATIVE_DRAWING_NAMESPACE) {
    private var roomName: String? = null
    private var fragmentActivity: FragmentActivity? = null

    override fun disconnect() {
        super.disconnect()
        mSocket.off(IN_PROGRESS_DRAWING_EVENT, onProgressDrawing)
    }

    override fun setFragmentActivity(fragmentAct: FragmentActivity) {
        fragmentActivity = fragmentAct
        setSocketListeners()
    }

    override fun joinRoom(roomName: String) {
        this.roomName = roomName
        super.joinRoom(roomName)
    }

    override fun setSocketListeners() {
        this.listenInProgressDrawingCommand()
    }

    fun sendInProgressDrawingCommand(drawingCommand: Any, type: String) {
        val socketToolCommand = SocketTool(
            type = type,
            roomName = this.roomName as String,
            drawingCommand = drawingCommand
        )

        val jsonSocket = JSONConvertor.convertToJSON(socketToolCommand)
        super.emit(IN_PROGRESS_DRAWING_EVENT, jsonSocket)
    }

    private fun listenInProgressDrawingCommand() {
        mSocket.on(IN_PROGRESS_DRAWING_EVENT, onProgressDrawing)
    }

    private val onProgressDrawing =
        Emitter.Listener { args ->
            fragmentActivity!!.runOnUiThread(Runnable {
                try {
                    val currentArg = args[0].toString()

                    // gotta deep check object type in a better way
                    if ("fill" in currentArg) {
                        // other client pressed the screen
                        val drawingCommand = JSONConvertor.getJSONObject(args, SyncCreateDrawing::class.java)
                        SynchronisationService.createCommand(drawingCommand)
                    } else {
                        // other client is drawing
                        val drawingUpdateCommand = JSONConvertor.getJSONObject(args, SyncUpdateDrawing::class.java)
                        SynchronisationService.drawAndUpdate(drawingUpdateCommand)
                    }
                } catch (e: JSONException) {
                    printMsg("listenInProgressDrawingCommand error: ${e.message}")
                    return@Runnable
                }
            })
        }
}