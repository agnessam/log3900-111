package com.example.colorimagemobile.services.socket

import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.adapter.ChannelsRecyclerAdapter
import com.example.colorimagemobile.classes.AbsSocket
import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.classes.NotificationSound.Notification
import com.example.colorimagemobile.models.ChatSocketModel
import com.example.colorimagemobile.services.chat.ChatAdapterService
import com.example.colorimagemobile.services.chat.ChatService
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants
import com.example.colorimagemobile.utils.Constants.SOCKETS
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject

object ChatSocketService: AbsSocket(SOCKETS.CHAT_NAMESPACE_NAME) {
    private var fragmentActivity: FragmentActivity? = null

    override fun disconnect() {
        mSocket.off(SOCKETS.TEXT_MESSAGE_EVENT_NAME, listenMessage)

        // leave each connected room
        TextChannelService.getConnectedChannels().forEach {
            leaveRoom(Constants.SocketRoomInformation(UserService.getUserInfo()._id, it.name))
        }

        super.disconnect()
    }

    override fun setFragmentActivity(fragmentAct: FragmentActivity) {
        fragmentActivity = fragmentAct
        setSocketListeners()
    }

    override fun setSocketListeners() {
        mSocket.on(SOCKETS.TEXT_MESSAGE_EVENT_NAME, listenMessage)
        mSocket.on(SOCKETS.ROOM_EVENT_NAME, listenHistory)
        mSocket.on(SOCKETS.LEAVE_ROOM_EVENT_NAME, listenLeaveRoom)
    }

    public override fun emit(event: String, data: Any) {
        super.emit(event, data)
    }

    fun sendMessage(message: JSONObject) {
        super.emit(SOCKETS.TEXT_MESSAGE_EVENT_NAME, message);
    }

    private val listenMessage =
        Emitter.Listener { args ->
            fragmentActivity!!.runOnUiThread(Runnable {
                try {
                    val currentArg = args[0].toString()
                    val message = JSONConvertor.getJSONObject(currentArg, ChatSocketModel::class.java)
                    ChatService.addMessage(message)
                    if (TextChannelService.isConnectedChannelInitialized()) {
                        // update current chat view
                        val currentRoom = TextChannelService.getCurrentChannel().name
                        if (message.roomName == currentRoom) {
                            ChatAdapterService.getChatMsgAdapter().addChatItem(message)
                        } else {
                            ChatService.unreadChannels.add(message.roomName)
                            ChatAdapterService.getChannelListAdapter().setUnreadChannels(ChatService.unreadChannels)
                        }

                        if (TextChannelService.isInConnectedChannels(message.roomName)) {
                            Notification().playNewMessageSound(fragmentActivity!!.applicationContext)
                        }
                    }
                } catch (e: JSONException) {
                    printMsg("listenMessage error: ${e.message}")
                    return@Runnable
                }
            })
        }

    private val listenHistory =
        Emitter.Listener { args ->
            fragmentActivity!!.runOnUiThread(Runnable {
                try {
                    val currentArg = args[0].toString()
                    val typeToken = object : TypeToken<List<ChatSocketModel>>() {}.type
                    val historyMessages = Gson().fromJson<List<ChatSocketModel>>(currentArg, typeToken)

                    if (historyMessages.isNotEmpty()) {
                        ChatService.addChat(historyMessages[0].roomName)
                        ChatService.setMessages(historyMessages[0].roomName, historyMessages.toMutableSet())
                    }
                } catch (e: JSONException) {
                    printMsg("listenHistory error: ${e.message}")
                    return@Runnable
                }
            })
        }

    private val listenLeaveRoom =
        Emitter.Listener { args ->
            fragmentActivity!!.runOnUiThread(Runnable {
                try {
                    val roomName = args[0].toString()
                    printMsg("listenLeaveRoom ${roomName}")

                    val socketInformation = Constants.SocketRoomInformation(UserService.getUserInfo()._id, roomName)
                    ChatSocketService.leaveRoom(socketInformation)
                } catch (e: JSONException) {
                    printMsg("listenLeaveRoom error: ${e.message}")
                    return@Runnable
                }
            })
        }
}