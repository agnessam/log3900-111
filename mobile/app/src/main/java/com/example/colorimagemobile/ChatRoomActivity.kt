package com.example.colorimagemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.databinding.ActivityChatRoomBinding
import com.example.colorimagemobile.ui.Message
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.lang.Exception
import com.google.gson.Gson


class ChatRoomActivity : AppCompatActivity() {

    val gson: Gson = Gson()
    lateinit var socket: Socket;
    lateinit var username: String;
    lateinit var roomName: String;

    val chatList: ArrayList<Message> = arrayListOf();
    lateinit var chatRoomAdapter: ChatRoomAdapter;

    private lateinit var chatRoomBinding: ActivityChatRoomBinding

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

        socket = SocketHandler.getSocket()
        socket.connect()
        socket.on(Socket.EVENT_CONNECT, onConnect)
        socket.on("text message", onNewMessageReception)
    }

    var onConnect = Emitter.Listener {
        //TODO remove this default
        roomName = "chatRoom"
        socket.emit("room", roomName)

    }

    var onNewMessageReception = Emitter.Listener {
        val message: Message = gson.fromJson(it[0], Message::class.java)
        message.viewType = MessageType.MESSAGE_RECEIVED.ordinal
        chatList.add(message)
        chatRoomAdapter.notifyItemInserted(chatList.size)

    }


    private fun sendMessage() {
        val content = chatRoomBinding.editMessage.text.toString()

    }


}