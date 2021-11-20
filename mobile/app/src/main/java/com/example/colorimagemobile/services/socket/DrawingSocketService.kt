package com.example.colorimagemobile.services.socket

import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.classes.AbsSocket
import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.services.drawing.SynchronisationService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants.SOCKETS
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.CONFIRM_DRAWING_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.CONFIRM_SELECTION_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.IN_PROGRESS_DRAWING_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.START_SELECTION_EVENT
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject

object DrawingSocketService: AbsSocket(SOCKETS.COLLABORATIVE_DRAWING_NAMESPACE) {
    private var roomName: String? = null
    private var fragmentActivity: FragmentActivity? = null

    override fun disconnect() {
        leaveRoom(this.roomName!!)
        mSocket.off(IN_PROGRESS_DRAWING_EVENT, onProgressDrawing)
        super.disconnect()
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
        this.listenConfirmDrawingCommand()
        this.listenStartSelectionCommand()
        this.listenConfirmSelectionCommand()
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

    fun sendConfirmDrawingCommand(drawingCommand: Any, type: String){
        val socketToolCommand = SocketTool(
            type = type,
            roomName = this.roomName as String,
            drawingCommand = drawingCommand
        )
        val jsonSocket = JSONConvertor.convertToJSON(socketToolCommand)
        super.emit(CONFIRM_DRAWING_EVENT, jsonSocket)

    }

    fun sendStartSelectionCommand(selectionStartCommand: SelectionData, type: String) {
        val selectionCommand = SocketTool(
            type = type,
            roomName = this.roomName as String,
            drawingCommand = selectionStartCommand,
        )
        val jsonSocket = JSONConvertor.convertToJSON(selectionCommand)
        super.emit(START_SELECTION_EVENT, jsonSocket)

    }

    fun sendConfirmSelectionCommand(confirmSelectionCommand: SelectionData, type: String) {
        val confirmSelectionCommand = SocketTool(
            type = type,
            roomName = this.roomName as String,
            drawingCommand = confirmSelectionCommand,
        )
        val jsonSocket = JSONConvertor.convertToJSON(confirmSelectionCommand)
        super.emit(CONFIRM_SELECTION_EVENT, jsonSocket)

    }

    private fun listenConfirmDrawingCommand(){
        mSocket.on(CONFIRM_DRAWING_EVENT, onConfirmDrawing)
    }


    private val onConfirmDrawing = Emitter.Listener { args ->
        fragmentActivity!!.runOnUiThread(Runnable {
            try {
                val toolCommandString = args[0].toString()
                SynchronisationService.removeFromPreview(toolCommandString)

            } catch (e: JSONException) {
                printMsg("listenInProgressDrawingCommand error: ${e.message}")
                return@Runnable
            }
        })
    }

    private fun listenInProgressDrawingCommand() {
        mSocket.on(IN_PROGRESS_DRAWING_EVENT, onProgressDrawing)
    }

    private val onProgressDrawing =
        Emitter.Listener { args ->
            fragmentActivity!!.runOnUiThread(Runnable {
                try {
                    val currentArg = args[0].toString()

                    // other client pressed the screen
                    SynchronisationService.draw(currentArg)

                } catch (e: JSONException) {
                    printMsg("listenInProgressDrawingCommand error: ${e.message}")
                    return@Runnable
                }
            })
        }

    private fun listenStartSelectionCommand() {
            mSocket.on(START_SELECTION_EVENT, startSelection)
    }

    private val startSelection =
        Emitter.Listener { args ->
            fragmentActivity!!.runOnUiThread(Runnable {
                try {
                    val currentArg = args[0].toString()
                    val selectionCommandJSON = JSONObject(currentArg)

                    val selectionCommand = JSONObject(selectionCommandJSON["drawingCommand"].toString())
                    val selectionCommandData = SocketTool(
                        type = selectionCommandJSON["type"] as String,
                        roomName = selectionCommandJSON["roomName"] as String,
                        drawingCommand = SelectionData(id = selectionCommand["id"].toString())
                    )
                    SynchronisationService.startSelection(selectionCommandData)

                } catch (e: JSONException) {
                    printMsg("startSelectionCommand error: ${e.message}")
                    return@Runnable
                }
            })
        }

    private fun listenConfirmSelectionCommand() {
        mSocket.on(CONFIRM_SELECTION_EVENT, confirmSelection)
    }

    private val confirmSelection =
        Emitter.Listener { args ->
            fragmentActivity!!.runOnUiThread(Runnable {
                try {
                    val currentArg = args[0].toString()
                    val selectionCommandJSON = JSONObject(currentArg)

                    val selectionCommand = JSONObject(selectionCommandJSON["drawingCommand"].toString())
                    val selectionCommandData = SocketTool(
                        type = selectionCommandJSON["type"] as String,
                        roomName = selectionCommandJSON["roomName"] as String,
                        drawingCommand = SelectionData(id = selectionCommand["id"].toString())
                    )
                    SynchronisationService.confirmSelection(selectionCommandData)

                } catch (e: JSONException) {
                    printMsg("confirmSelectionCommand error: ${e.message}")
                    return@Runnable
                }
            })
        }
}