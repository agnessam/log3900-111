package com.example.colorimagemobile

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorimagemobile.databinding.ActivityChatRoomBinding
import com.example.colorimagemobile.ui.Message
import com.example.colorimagemobile.ui.MessageWithType
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.lang.Exception
import com.google.gson.Gson
import java.time.LocalTime


class ChatRoomActivity : AppCompatActivity() {

    val gson: Gson = Gson()
    lateinit var socket: Socket
    lateinit var username: String
    lateinit var roomName: String

    val chatList: ArrayList<MessageWithType> = arrayListOf()
    lateinit var chatRoomAdapter: ChatRoomAdapter

    private lateinit var chatRoomBinding: ActivityChatRoomBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatRoomBinding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(chatRoomBinding.root)

        chatRoomAdapter = ChatRoomAdapter(layoutInflater, chatList)
        chatRoomBinding.recyclerView.adapter = chatRoomAdapter
        chatRoomBinding.recyclerView.layoutManager = LinearLayoutManager(this)


        try {
            username = intent.getStringExtra("username")!!
            Log.d("Oncreate","Oncreate fonction username= "+username) // add for test purpose
        } catch (e: Exception) {
            Log.d("Oncreate","error message= "+e)
            e.printStackTrace()
        }

        SocketHandler.setSocket()
      //  socket.connect()
        SocketHandler.establishConnection()
        socket = SocketHandler.getSocket()
        Log.d("Oncreate","+++++++++++++++++++++++++++++contenu du socket= "+ socket)
        socket.on(Socket.EVENT_CONNECT, onConnect)

        chatRoomBinding.sendButton.setOnClickListener { sendMessage() }

        socket.on("text message", onNewMessageReception)
    }

    private var onConnect = Emitter.Listener {
        //TODO remove this default
        roomName = "chatRoom"
        socket.emit("room", roomName)
        Log.d("Connected server","CONNECTEDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD")
    }

    private var onNewMessageReception = Emitter.Listener {
        val message: MessageWithType = gson.fromJson(it[0].toString(), MessageWithType::class.java)
        Log.d("Onnewmessage","GOT NEW MESSAGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"+message)
        message.viewType = MessageType.MESSAGE_RECEIVED.ordinal
        chatList.add(message)
        chatRoomAdapter.notifyItemInserted(chatList.size)
        chatRoomBinding.recyclerView.smoothScrollToPosition(chatList.size - 1)

    }

//    override fun OnClick() {
//
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMessage() {
        val content = chatRoomBinding.editMessage.text.toString()
        //TODO replace roomname
        val message = Message(content, LocalTime.now().toString(), username, "chatRoom")
        val jsonMessage = gson.toJson(message)
        socket.emit("text message", message)
        Log.d("sendmessage","SENDINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"+jsonMessage)
        Log.d("autheur","SENDINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"+ username.toString())
    }

}
