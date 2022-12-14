package com.example.colorimagemobile

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.adapter.ChatMessageRecyclerAdapter
import com.example.colorimagemobile.services.SocketHandler
import com.example.colorimagemobile.ui.login.LoginActivity
import com.example.colorimagemobile.utils.CommonFun.Companion.closeKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.Constants
import com.example.colorimagemobile.utils.Constants.Companion.DEBUG_KEY
import com.example.colorimagemobile.utils.Constants.Companion.DEFAULT_ROOM_NAME
import com.example.colorimagemobile.utils.Constants.Companion.TEXT_MESSAGE_EVENT_NAME
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList
import io.socket.client.Socket
import org.json.JSONException
import io.socket.emitter.Emitter
import java.text.SimpleDateFormat

class ChatActivity : AppCompatActivity() {
    var username: String = ""

//    private var messageArray: ArrayList<Message> = arrayListOf<Message>()
    private lateinit var mSocket: Socket

    // layout manager and adapter
    private lateinit var recyclerView: RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapterChatMessage: RecyclerView.Adapter<ChatMessageRecyclerAdapter.ViewHolder>? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val sharedPref = getSharedPreferences(Constants.STORAGE_KEY.MAIN, Context.MODE_PRIVATE)
        val token = sharedPref.getString(Constants.STORAGE_KEY.TOKEN, "")
        this.username = ""

        // check if token is valid! if not, redirect to /login
        if (token == "" || token == null) {
            redirectTo(this@ChatActivity, LoginActivity::class.java)
        }

        // update chat text by showing username
        val chatText: TextView = findViewById(R.id.chatText)
        chatText.append(" ${this.username}!")

        // init socket
        SocketHandler.setSocket()
        mSocket = SocketHandler.getSocket()
        mSocket.connect()
        mSocket.emit("room", DEFAULT_ROOM_NAME)
        mSocket.on(TEXT_MESSAGE_EVENT_NAME, onNewMessage)





        // chat adapters
        layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager

//        adapterChatMessage = ChatMessageRecyclerAdapter(messageArray, this.username)
        recyclerView.adapter = adapterChatMessage

        // listeners
        setLogOutListener()
        setOnScreenTapListener()

        // send button
        val sendChatBtn: Button = findViewById(R.id.sendBtn)
        sendChatBtn.setOnClickListener { executeChatSend() }

        // when pressed on Enter
        val editText: EditText = findViewById(R.id.chatTextInput)
        editText.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                executeChatSend()
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        mSocket.off(TEXT_MESSAGE_EVENT_NAME, onNewMessage)
    }

    private fun setOnScreenTapListener() {
        val chatMain: ConstraintLayout = findViewById(R.id.chatMain)
        chatMain.setOnTouchListener { v, event ->
            closeKeyboard(this@ChatActivity)
        }

        recyclerView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> closeKeyboard(this@ChatActivity)
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    private fun setLogOutListener() {
    }

    private fun executeChatSend() {
        val chatText: EditText = findViewById(R.id.chatTextInput)
        val chatTextInput = chatText.text.toString()

        closeKeyboard(this@ChatActivity)

        // not connected to server
        if (!mSocket.isActive || !mSocket.connected()) {
            printToast(applicationContext, "Error: Server is offline!")
            return
        }

        if (chatTextInput.trim().length === 0) {
            printToast(applicationContext, "Please enter a message first!")
            return
        }

        val currentTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
//        val newMessage = Message(chatTextInput, currentTime, this.username, DEFAULT_ROOM_NAME)

        // convert message class to JSON format but server receives it as String
        val gson = Gson()
        val jsonMessageData = gson.toJson(null)

        mSocket.emit("room", DEFAULT_ROOM_NAME)
        mSocket.emit(TEXT_MESSAGE_EVENT_NAME, jsonMessageData)

        // clear text
        chatText.text = null
    }

    // listens for incoming messages
    private val onNewMessage =
        Emitter.Listener { args ->
            runOnUiThread(Runnable {
                val gson: Gson = Gson()
//                val message = gson.fromJson(args[0].toString(), Message::class.java)

                val message = null
                try {
//                    this.addMessage(message)
                } catch (e: JSONException) {
                    Log.d(DEBUG_KEY, "Error occurred while receiving incoming message ${e.message}")
                    return@Runnable
                }
            })
        }

    // add new chat message
//    private fun addMessage(newMessage: Message) {
//        messageArray.add(newMessage)
//        adapterChatMessage?.notifyDataSetChanged()
//
//        // if only we send a msg, scroll down
//        if(newMessage.author.equals(this.username)) {
//            recyclerView.scrollToPosition(messageArray.size - 1);
//        }
//    }
}
