package com.example.colorimagemobile.ui.home.fragments.chat.chatBox

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.models.ChatSocketModel
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.services.chat.ChatAdapterService
import com.example.colorimagemobile.services.chat.ChatService
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.services.socket.ChatSocketService
import com.example.colorimagemobile.utils.CommonFun.Companion.closeKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.onEnterKeyPressed

class ChatMessageBoxFragment : Fragment(R.layout.fragment_chat_message_box) {

    private lateinit var myView: View
    private lateinit var channel: TextChannelModel.AllInfo
    private lateinit var chatMsgEditText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view
        channel = TextChannelService.getCurrentChannel()
        chatMsgEditText = myView.findViewById(R.id.chat_text_input)

        connectToSocket()
        updateUI()
        setListeners()
    }

    override fun onStop() {
        super.onStop()
        closeSockets()
    }

    override fun onDestroy() {
        super.onDestroy()
        closeSockets()
    }

    // leave sockets here
    private fun closeSockets() {
//        printMsg("${channel.name} left chat room")
    }

    private fun connectToSocket() {
        ChatSocketService.connect()
        ChatSocketService.setFragmentActivity(requireActivity())
        ChatSocketService.joinRoom(channel.name)
    }

    private fun updateUI() {
        myView.findViewById<TextView>(R.id.chat_username).text = channel.name

        // set up Recycler View
        val recyclerView = myView.findViewById<RecyclerView>(R.id.chat_message_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ChatAdapterService.getAdapter()

        // update chat messages
        val currentChat = ChatService.getChannelMessages(channel.name) as MutableSet<ChatSocketModel>
        ChatAdapterService.getAdapter().setChat(currentChat)
    }

    private fun setListeners() {
        val sendBtn: Button = myView.findViewById(R.id.chat_sent_btn)

        sendBtn.setOnClickListener { sendChat() }
        onEnterKeyPressed(chatMsgEditText) { sendChat() }
    }

    private fun sendChat() {
        closeKeyboard(requireActivity())
        val newMessage = ChatService.createMessage(chatMsgEditText.text.toString())
        val newMessageJSON = JSONConvertor.convertToJSON(newMessage)
        ChatSocketService.sendMessage(newMessageJSON)

        chatMsgEditText.text = null
    }
}