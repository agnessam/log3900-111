package com.example.colorimagemobile.services.socket

import android.content.Context
import android.graphics.Bitmap
import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.classes.AbsSocket
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.repositories.DrawingRepository
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.services.drawing.SynchronisationService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.ui.home.fragments.gallery.GalleryDrawingFragment
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants
import com.example.colorimagemobile.utils.Constants.SOCKETS
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.CONFIRM_DRAWING_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.CONFIRM_SELECTION_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.IN_PROGRESS_DRAWING_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.START_SELECTION_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.TRANSFORM_SELECTION_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.UPDATE_DRAWING_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.UPDATE_DRAWING_NOTIFICATION
import io.socket.client.Ack
import io.socket.emitter.Emitter
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.awaitResponse
import java.lang.Runnable

object DrawingSocketService: AbsSocket(SOCKETS.COLLABORATIVE_DRAWING_NAMESPACE) {
    private var roomName: String? = null
    private var fragmentActivity: FragmentActivity? = null
    private val drawingRepository: DrawingRepository = DrawingRepository()

    override fun disconnect() {
        leaveRoom(this.roomName!!)
        mSocket.off(IN_PROGRESS_DRAWING_EVENT, onProgressDrawing)
        super.disconnect()
    }

    override fun setFragmentActivity(fragmentAct: FragmentActivity) {
        fragmentActivity = fragmentAct
        setSocketListeners()
    }

    override fun joinRoom(roomInformation: Constants.SocketRoomInformation) {
        this.roomName = roomInformation.roomName
        val socketInformation = Constants.SocketRoomInformation(UserService.getUserInfo()._id, this.roomName!!)
        super.joinRoom(socketInformation)
    }

    override fun setSocketListeners() {
        this.listenInProgressDrawingCommand()
        this.listenConfirmDrawingCommand()
        this.listenStartSelectionCommand()
        this.listenConfirmSelectionCommand()
        this.listenTransformSelectionCommand()
        this.listenUpdateDrawingRequest()
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

    fun sendConfirmDrawingCommand(drawingCommand: Any, type: String) {
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

    fun sendTransformSelectionCommand(transformSelectionCommand: Any, type: String) {
        val transformCommand = SocketTool(
            type = type,
            roomName = this.roomName as String,
            drawingCommand = transformSelectionCommand,
        )
        val jsonSocket = JSONConvertor.convertToJSON(transformCommand)
        super.emit(TRANSFORM_SELECTION_EVENT, jsonSocket)
    }

    private fun listenConfirmDrawingCommand() {
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

                    val selectionCommand =
                        JSONObject(selectionCommandJSON["drawingCommand"].toString())
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

                    val selectionCommand =
                        JSONObject(selectionCommandJSON["drawingCommand"].toString())
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

    private fun listenTransformSelectionCommand() {
        mSocket.on(TRANSFORM_SELECTION_EVENT, transformSelection)
    }

    private val transformSelection = Emitter.Listener { args ->
        fragmentActivity!!.runOnUiThread(Runnable {
            try {
                val currentArg = args[0].toString()
                val transformCommandJSON = JSONObject(currentArg)

                val transformCommand = JSONObject(transformCommandJSON["drawingCommand"].toString())
                val transformCommandData = SocketTool(
                    type = transformCommandJSON["type"] as String,
                    roomName = transformCommandJSON["roomName"] as String,
                    drawingCommand = when (transformCommandJSON["type"] as String) {
                        "SelectionResize" -> JSONConvertor.getJSONObject(
                            transformCommandJSON["drawingCommand"].toString(),
                            ResizeData::class.java
                        )
                        "Translation" -> JSONConvertor.getJSONObject(
                            transformCommandJSON["drawingCommand"].toString(),
                            TranslateData::class.java
                        )
                        else -> throw Exception("drawingCommand for transform selection is invalid")
                    }
                )
                SynchronisationService.transformSelection(transformCommandData)

            } catch (e: JSONException) {
                printMsg("transformSelectionCommand error: ${e.message}")
                return@Runnable
            }
        })
    }

    private fun listenUpdateDrawingRequest() {
        mSocket.on(UPDATE_DRAWING_EVENT, updateDrawingRequestListen)
    }

    private var updateDrawingRequestListen = Emitter.Listener { args ->
        val responseJSON = JSONObject(args[0].toString())
        val newUserId = responseJSON["newUserId"] as String

        GlobalScope.launch(newSingleThreadContext("sendUpdateDrawingRequest")){
            updateDrawingRequest(newUserId)
        }
    }

    private suspend fun updateDrawingRequest(newUserId: String){
        val drawing = drawingRepository.saveCurrentDrawing()
        if(drawing != null){
            sendDrawingUpdatedNotification(newUserId)
        }
    }

    private fun sendDrawingUpdatedNotification(clientSocketId: String) {
        mSocket.emit(UPDATE_DRAWING_NOTIFICATION, clientSocketId);
    }

    fun sendGetUpdateDrawingRequest(context: Context, drawingMenus: ArrayList<DrawingMenuData>, position: Int) {
        mSocket.emit(UPDATE_DRAWING_EVENT, roomName, Ack{
            args ->
            val responseJSON = JSONObject(args[0].toString())
            if(responseJSON["status"] is String){
                runBlocking{
                    processUpdateDrawingRequestCallback(responseJSON["status"] as String, context, drawingMenus, position)
                }
            }
        })
    }

    private suspend fun processUpdateDrawingRequestCallback(status: String, context: Context, drawingMenus: ArrayList<DrawingMenuData>, position: Int){
        if (status == "One User") {
            val drawingId = DrawingService.getCurrentDrawingID()
            if (drawingId != null) {
                val response = drawingRepository.getDrawing(DrawingService.getCurrentDrawingID()!!)
                if(response != null){
                    val imageConvertor = ImageConvertor(context)
                    val svgString = imageConvertor.getSvgAsString(response.dataUri)
                    drawingMenus[position].svgString = svgString
                }
            }
        }
    }
}