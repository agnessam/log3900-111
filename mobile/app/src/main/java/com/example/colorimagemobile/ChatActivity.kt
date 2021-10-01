package com.example.colorimagemobile

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.adapter.RecyclerAdapter
import com.example.colorimagemobile.handler.RetrofitInstance
import com.example.colorimagemobile.handler.SocketHandler
import com.example.colorimagemobile.model.Message
import com.example.colorimagemobile.model.User
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.redirectTo
import com.example.colorimagemobile.utils.Constants
import com.example.colorimagemobile.utils.Constants.Companion.DEBUG_KEY
import com.example.colorimagemobile.utils.Constants.Companion.DEFAULT_ROOM_NAME
import com.example.colorimagemobile.utils.Constants.Companion.LOCAL_STORAGE_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_TOKEN_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_USERNAME_KEY
import com.example.colorimagemobile.utils.Constants.Companion.TEXT_MESSAGE_EVENT_NAME
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import io.socket.client.Socket
import org.json.JSONObject
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import org.json.JSONException
import io.socket.emitter.Emitter

class ChatActivity : AppCompatActivity() {
    var username: String = ""

    private var messageArray: ArrayList<Message> = arrayListOf<Message>()
    private lateinit var mSocket: Socket

    // layout manager and adapter
    private lateinit var recyclerView: RecyclerView
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val sharedPref = getSharedPreferences(Constants.LOCAL_STORAGE_KEY, Context.MODE_PRIVATE)
        val token = sharedPref.getString(SHARED_TOKEN_KEY, "")
        this.username = sharedPref.getString(SHARED_USERNAME_KEY, "") as String

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

        // listeners
        setLogOutListener()
        setAddChatListener()

        // chat adapters
        layoutManager = LinearLayoutManager(this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager

        adapter = RecyclerAdapter(messageArray, this.username)
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        mSocket.off(TEXT_MESSAGE_EVENT_NAME, onNewMessage)
    }

    private fun setLogOutListener() {
        val logOutBtn: Button = findViewById(R.id.logoutBtn)

        logOutBtn.setOnClickListener(View.OnClickListener {
            // log out -> POST /logout
            val user = User(this.username, "kesh")

            RetrofitInstance.HTTP.logoutUser(user).enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (!response.isSuccessful) {
                        Log.d("HTTP request error", response.message())
                        printToast(applicationContext, "An error occurred!")
                        return
                    }

                    printToast(applicationContext, "Logging you out ${user.username}!")

                    // remove items from "local storage"
                    val sharedPref = getSharedPreferences(LOCAL_STORAGE_KEY, Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.clear().apply()

                    // redirect to /login
                    redirectTo(this@ChatActivity, LoginActivity::class.java)
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.d("User failed to log out", t.message!!)
                    printToast(applicationContext, "Failed to logout!\nAn error occurred")
                }
            })
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAddChatListener() {
        val sendChatBtn: Button = findViewById(R.id.sendBtn)

        sendChatBtn.setOnClickListener(View.OnClickListener {
            val chatText: EditText = findViewById(R.id.chatTextInput)
            val chatTextInput = chatText.text.toString()

            // not connected to server
            if (!mSocket.isActive || !mSocket.connected()) {
                printToast(applicationContext, "Error: Server is offline!")
                return@OnClickListener
            }

            if (chatTextInput.length === 0) {
                printToast(applicationContext, "Please enter a message first!")
                return@OnClickListener
            }

            val currentTimestamp =
                DateTimeFormatter
                    .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                    .withZone(ZoneOffset.UTC)
                    .format(Instant.now())

            val newMessage = Message(chatTextInput, currentTimestamp, this.username, DEFAULT_ROOM_NAME)

            // convert message class to JSON format
            val jsonData = JSONObject(newMessage.toString())

            mSocket.emit("room", DEFAULT_ROOM_NAME)
            mSocket.emit(TEXT_MESSAGE_EVENT_NAME, jsonData)

            // clear text
            chatText.text = null
        })
    }

    // listens for incoming messages
    private val onNewMessage =
        Emitter.Listener { args ->
            runOnUiThread(Runnable {
                val gson: Gson = Gson()
                val message = gson.fromJson(args[0].toString(), Message::class.java)

                try {
                    this.addMessage(message)
                } catch (e: JSONException) {
                    Log.d(DEBUG_KEY, "Error occurred while receiving incoming message ${e.message}")
                    return@Runnable
                }
            })
        }

    // add new chat message
    private fun addMessage(newMessage: Message) {
        messageArray.add(newMessage)
        adapter?.notifyDataSetChanged()

        // if only we send a msg, scroll down
        if(newMessage.author.equals(this.username)) {
            recyclerView.scrollToPosition(messageArray.size - 1);
        }
    }
}
