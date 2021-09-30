package com.example.colorimagemobile

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
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
import com.example.colorimagemobile.utils.Constants.Companion.DEFAULT_ROOM_NAME
import com.example.colorimagemobile.utils.Constants.Companion.LOCAL_STORAGE_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_TOKEN_KEY
import com.example.colorimagemobile.utils.Constants.Companion.SHARED_USERNAME_KEY
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


class ChatActivity : AppCompatActivity() {
    var username: String = ""

    private var messageArray: ArrayList<Message> = arrayListOf<Message>()
    private lateinit var mSocket: Socket

    // layout manager and adapter
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

        // listeners
        setLogOutListener()
        this.setAddChatListener()

        // socket
        SocketHandler.setSocket()
        mSocket = SocketHandler.getSocket()
        mSocket.connect()

        // chat adapters
        layoutManager = LinearLayoutManager(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager

        adapter = RecyclerAdapter(messageArray)
        recyclerView.adapter = adapter
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

            val currentTimestamp =
                DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now())

            val sharedPref = getSharedPreferences(LOCAL_STORAGE_KEY, Context.MODE_PRIVATE)
            val username = sharedPref.getString(SHARED_USERNAME_KEY, "").toString()

            val newMessage = Message("chatText.text.toString()", currentTimestamp, username, DEFAULT_ROOM_NAME)

            messageArray.add(newMessage)
            adapter?.notifyDataSetChanged()

            val jsonData = JSONObject(newMessage.toString())

            mSocket.emit("room", DEFAULT_ROOM_NAME)
            mSocket.emit("text message", jsonData)
        })
    }
}