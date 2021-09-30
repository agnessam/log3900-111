package com.example.colorimagemobile

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

        SocketHandler.setSocket()
        socket = SocketHandler.getSocket()
        socket.connect()
        socket.on(Socket.EVENT_CONNECT, onConnect)
        socket.on("text message", onNewMessageReception)

        chatRoomBinding.sendButton.setOnClickListener { sendMessage() }
    }

    private var onConnect = Emitter.Listener {
        //TODO remove this default
        roomName = "chatRoom"
        socket.emit("room", roomName)
        print("CONNECTEDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD")
    }

    private var onNewMessageReception = Emitter.Listener {
        val message: MessageWithType = gson.fromJson(it[0].toString(), MessageWithType::class.java)
        message.viewType = MessageType.MESSAGE_RECEIVED.ordinal
        chatList.add(message)
        chatRoomAdapter.notifyItemInserted(chatList.size)
        chatRoomBinding.recyclerView.smoothScrollToPosition(chatList.size - 1)
        print("GOT NEW MESSAGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"+message)
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
        socket.emit("text message", jsonMessage)
        print("SENDINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG"+jsonMessage)
    }

}
