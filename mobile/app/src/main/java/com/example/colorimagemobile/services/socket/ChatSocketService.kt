package com.example.colorimagemobile.services.socket

import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.adapter.ChannelsRecyclerAdapter
import com.example.colorimagemobile.classes.AbsSocket
import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.models.ChatSocketModel
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.services.NotificationService
import com.example.colorimagemobile.services.chat.ChatAdapterService
import com.example.colorimagemobile.services.chat.ChatService
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants.SOCKETS
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject

object ChatSocketService: AbsSocket(SOCKETS.CHAT_NAMESPACE_NAME) {

    private var fragmentActivity: FragmentActivity? = null
    private var count : Int = 0
    private var pendingNotification : HashMap<String, Int> = HashMap()
    private lateinit var messageArray : ArrayList<TextChannelModel.AllInfo>



    override fun disconnect() {
        mSocket.off(SOCKETS.TEXT_MESSAGE_EVENT_NAME, listenMessage)

        // leave each connected room
        TextChannelService.getConnectedChannels().forEach {
            leaveRoom(it.name)
        }

        super.disconnect()
    }

    override fun setFragmentActivity(fragmentAct: FragmentActivity) {
        fragmentActivity = fragmentAct
        setSocketListeners()
    }

    override fun setSocketListeners() {
        mSocket.on(SOCKETS.TEXT_MESSAGE_EVENT_NAME, listenMessage)
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

                    val currentRoom = TextChannelService.getCurrentChannel().name
                    if (message.roomName == currentRoom) {
                        ChatAdapterService.getChatMsgAdapter().addChatItem(message)
                        if (message.author != UserService.getUserInfo().username){
                            printMsg("print lka map:Message.roomname ++++++=========+++++++======++++ "+ pendingNotification[message.roomName])
                            pendingNotification.keys.forEach { key ->
                                if (pendingNotification[key]==null){
                                    printMsg("le key is  = "+key)
                                    printMsg("print value de chaque key  = "+ pendingNotification[key])
                                    printMsg("le compte est nul")
                                    count = 0
                                }
                                else{
                                    printMsg("le room existe deja dans la map")
                                    count = pendingNotification[key]!!

                                }
                            }
                            count = count + 1
                            NotificationService.setCounter(count)
                            NotificationService.playSound(fragmentActivity!!.applicationContext)
                            printMsg("inside author not the sender: value count $count")
                            pendingNotification[message.roomName]= count
                            printMsg("inside author not the sender: value hashmap============================================================="+pendingNotification[message.roomName])
                            NotificationService.addToPendingMessage(pendingNotification)
                            printMsg("inside author not the sender: value hash de notif service $NotificationService.getPendingNotification()")
                            fragmentActivity!!.invalidateOptionsMenu()
                            printMsg("inside author not the sender: before notifydatachange")
//                            ChannelsRecyclerAdapter().notifyDataSetChanged()
                        }

                    }
                } catch (e: JSONException) {
                    printMsg("listenMessage error: ${e.message}")
                    return@Runnable
                }
                ChannelsRecyclerAdapter().notifyDataSetChanged()
            } )
        }

//    private fun notifyUser(){
//        count = count + 1
//        printMsg("in notifyuser before setcounter value= "+count)
//        NotificationService.setCounter(count)
//        printMsg("in notifyuser after setcounter value= "+NotificationService.getCounter())
//        NotificationService.playSound(fragmentActivity!!.applicationContext)
//        printMsg("in notifyuser after song play= "+NotificationService.getCounter())
//        fragmentActivity!!.invalidateOptionsMenu()
//        NotificationService.createNotificationChannel(fragmentActivity!!.applicationContext)
//        NotificationService.sendNotification(fragmentActivity!!.applicationContext)
//    }

}