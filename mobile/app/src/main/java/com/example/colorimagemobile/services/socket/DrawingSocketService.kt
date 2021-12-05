package com.example.colorimagemobile.services.socket

import android.util.Base64
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
import com.example.colorimagemobile.services.drawing.toolsAttribute.RGB
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.ui.home.fragments.gallery.GalleryDrawingFragment
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants
import com.example.colorimagemobile.utils.Constants.SOCKETS
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.CONFIRM_DRAWING_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.CONFIRM_SELECTION_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.DELETE_SELECTION_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.FETCH_DRAWING_NOTIFICATION
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.IN_PROGRESS_DRAWING_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.LINE_WIDTH_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.PRIMARY_COLOR_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.SECONDARY_COLOR_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.START_SELECTION_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.TRANSFORM_SELECTION_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.UPDATE_DRAWING_EVENT
import com.example.colorimagemobile.utils.Constants.SOCKETS.Companion.UPDATE_DRAWING_NOTIFICATION
import io.socket.client.Ack
import io.socket.emitter.Emitter
import kotlinx.coroutines.*
import kotlinx.coroutines.internal.SynchronizedObject
import org.json.JSONException
import org.json.JSONObject
import java.lang.Runnable
import java.nio.charset.StandardCharsets

object DrawingSocketService: AbsSocket(SOCKETS.COLLABORATIVE_DRAWING_NAMESPACE) {
    private var roomName: String? = null
    private var fragmentActivity: FragmentActivity? = null
    private val drawingRepository: DrawingRepository = DrawingRepository()

    private var drawingMenus: ArrayList<DrawingMenuData>? = null
    private var position: Int? = null
    private var destination: Int? = null

    private var openListenersHaveBeenInstantiated = false
    private var drawingHasBeenInitialized = false

    override fun leaveRoom(roomInformation: Constants.SocketRoomInformation){

        this.drawingMenus = null
        this.position = null
        this.destination = null

        super.leaveRoom(roomInformation)
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

    public override fun setSocketListeners() {
        if(!openListenersHaveBeenInstantiated){
            this.listenUpdateDrawingRequest()
            this.listenFetchDrawingNotification()
            openListenersHaveBeenInstantiated = true
        }
    }

    fun setDrawingCommandSocketListeners(){
        if(!drawingHasBeenInitialized){
            this.listenInProgressDrawingCommand()
            this.listenConfirmDrawingCommand()
            this.listenStartSelectionCommand()
            this.listenConfirmSelectionCommand()
            this.listenTransformSelectionCommand()
            this.listenDeleteSelectionCommand()
            this.listenObjectPrimaryColorChange()
            this.listenObjectSecondaryColorChange()
            this.listenLineWidthChange()
            drawingHasBeenInitialized = true
        }
    }

    fun joinCurrentDrawingRoom() {
        if (DrawingService.getCurrentDrawingID() != null) {
            connect()

            val socketInformation =
                Constants.SocketRoomInformation(UserService.getUserInfo()._id, DrawingService.getCurrentDrawingID()!!)
            joinRoom(socketInformation)
        }
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

    fun sendDeleteSelectionCommand(objectToDeleteId: String){
        val deleteCommand = SocketTool(
            type = "Delete",
            roomName = this.roomName as String,
            drawingCommand = DeleteData(objectToDeleteId),
        )
        val jsonSocket = JSONConvertor.convertToJSON(deleteCommand)
        super.emit(DELETE_SELECTION_EVENT, jsonSocket)
    }

    private fun listenDeleteSelectionCommand() {
        mSocket.on(DELETE_SELECTION_EVENT, deleteSelection)
    }

    private var deleteSelection = Emitter.Listener { args ->
        fragmentActivity!!.runOnUiThread(Runnable {
            val  responseJSON = JSONObject(args[0].toString())
            val deleteSelectionData = SocketTool(
                type = responseJSON["type"] as String,
                roomName = responseJSON["roomName"] as String,
                drawingCommand = JSONConvertor.getJSONObject(
                    responseJSON["drawingCommand"].toString(),
                    DeleteData::class.java)
            )

            SynchronisationService.deleteSelection(deleteSelectionData)
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

        runBlocking {
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

    private fun listenFetchDrawingNotification(){
        mSocket.on(FETCH_DRAWING_NOTIFICATION) {
            if(drawingMenus != null && destination != null && position != null && fragmentActivity != null){
                getDrawingAndUpdateGUI(fragmentActivity!!, drawingMenus!!, position!!, destination!!)
            }
        }
    }

    fun sendGetUpdateDrawingRequest(drawingMenus: ArrayList<DrawingMenuData>, position: Int, destination: Int) {
        mSocket.emit(UPDATE_DRAWING_EVENT, roomName, Ack{
            args ->
            this.drawingMenus = drawingMenus
            this.position = position
            this.destination = destination

            val responseJSON = JSONObject(args[0].toString())
            if(responseJSON["status"] is String && responseJSON["status"] == "One User"){
                if(fragmentActivity != null){
                    getDrawingAndUpdateGUI(fragmentActivity!!, drawingMenus, position, destination)
                }
            }
        })
    }

    private suspend fun processUpdateDrawingRequestCallback(drawingMenus: ArrayList<DrawingMenuData>, position: Int){
        val drawingId = DrawingService.getCurrentDrawingID()
        if (drawingId != null) {
            val response = drawingRepository.getDrawing(DrawingService.getCurrentDrawingID()!!)
            if(response != null){
                val svgString = getSvgAsString(response.dataUri)
                drawingMenus[position].svgString = svgString
            }
        }
    }

    private fun getDrawingAndUpdateGUI(context: FragmentActivity, drawingMenus: ArrayList<DrawingMenuData>, position: Int, destination: Int) {
        GlobalScope.launch{
            val processUpdateDrawingRequestCallbackJob = launch{
                processUpdateDrawingRequestCallback(drawingMenus, position)
            }
            withContext(Dispatchers.Main){
                processUpdateDrawingRequestCallbackJob.join()
                DrawingObjectManager.createDrawableObjects(drawingMenus[position].svgString)
                MyFragmentManager(context).open(destination, GalleryDrawingFragment())
                CanvasUpdateService.asyncInvalidate()
            }
        }
    }

    private fun getSvgAsString(dataURI: String): String {
        if (!dataURI.contains(ImageConvertor.BASE_64_URI)) return dataURI

        val base64Data = dataURI.replace(ImageConvertor.BASE_64_URI, "");
        val imageBytes = Base64.decode(base64Data, Base64.DEFAULT);
        return String(imageBytes, StandardCharsets.UTF_8)
    }

    fun sendObjectPrimaryColorChange(objectId: String, color: RGB, opacity: Float) {
        roomName ?: return
        val colorData = ColorData(objectId, color, opacity, roomName!!)
        val jsonSocket = JSONConvertor.convertToJSON(colorData)
        super.emit(PRIMARY_COLOR_EVENT, jsonSocket)
    }

    private fun listenObjectPrimaryColorChange() {
        mSocket.on(PRIMARY_COLOR_EVENT, primaryColorListener)
    }

    private var primaryColorListener = Emitter.Listener { args ->
        fragmentActivity!!.runOnUiThread(Runnable {
            val  responseJSON = JSONObject(args[0].toString())
            val colorData = ColorData(
                id = responseJSON["id"] as String,
                color = JSONConvertor.getJSONObject(responseJSON["color"].toString(), RGB::class.java),
                opacity = responseJSON["opacity"].toString().toFloat(),
                roomName = responseJSON["roomName"] as String
            )
            SynchronisationService.setObjectPrimaryColor(colorData)
        })
    }

    private fun listenObjectSecondaryColorChange() {
        mSocket.on(SECONDARY_COLOR_EVENT, secondaryColorListener)
    }

    private var secondaryColorListener = Emitter.Listener { args ->
        fragmentActivity!!.runOnUiThread(Runnable {
            val  responseJSON = JSONObject(args[0].toString())
            val colorData = ColorData(
                id = responseJSON["id"] as String,
                color = JSONConvertor.getJSONObject(responseJSON["color"].toString(), RGB::class.java),
                opacity = responseJSON["opacity"].toString().toFloat(),
                roomName = responseJSON["roomName"] as String
            )
            SynchronisationService.setObjectSecondaryColor(colorData)
        })
    }

    fun sendObjectSecondaryColorChange(objectId: String, color: RGB, opacity: Float) {
        roomName ?: return
        val colorData = ColorData(objectId, color, opacity, roomName!!)
        val jsonSocket = JSONConvertor.convertToJSON(colorData)
        super.emit(SECONDARY_COLOR_EVENT, jsonSocket)
    }

    private fun listenLineWidthChange() {
        mSocket.on(LINE_WIDTH_EVENT, lineWidthListener)
    }

    private var lineWidthListener = Emitter.Listener { args ->
        fragmentActivity!!.runOnUiThread(Runnable {
            val  responseJSON = JSONObject(args[0].toString())
            val lineWidthData = LineWidthData(
                id =  responseJSON["id"].toString(),
                roomName = responseJSON["roomName"].toString(),
                lineWidth = responseJSON["lineWidth"].toString().toInt()
            )
            SynchronisationService.setSelectionLineWidth(lineWidthData)
        })
    }

    fun sendLineWidthChange(objectId: String, lineWidth: Int) {
        var lineWidthData = LineWidthData(objectId, roomName!!, lineWidth)
        val jsonSocket = JSONConvertor.convertToJSON(lineWidthData)
        super.emit(LINE_WIDTH_EVENT, jsonSocket)
    }
}