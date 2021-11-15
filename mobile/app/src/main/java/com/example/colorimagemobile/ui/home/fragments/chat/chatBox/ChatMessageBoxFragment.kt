package com.example.colorimagemobile.ui.home.fragments.chat.chatBox

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.bottomsheets.DeleteChannelConfirmationBottomSheet
import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.ChatSocketModel
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.services.chat.ChatAdapterService
import com.example.colorimagemobile.services.chat.ChatService
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.services.socket.ChatSocketService
import com.example.colorimagemobile.utils.CommonFun.Companion.closeKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.onEnterKeyPressed
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.Constants.Companion.GENERAL_CHANNEL_NAME

class ChatMessageBoxFragment : Fragment(R.layout.fragment_chat_message_box) {

    private lateinit var myView: View
    private lateinit var channel: TextChannelModel.AllInfo
    private lateinit var chatMsgEditText: EditText
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view
        channel = TextChannelService.getCurrentChannel()
        chatMsgEditText = myView.findViewById(R.id.chat_text_input)
        recyclerView = myView.findViewById(R.id.chat_message_recycler_view)

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

        // remove leaveRoom button for General
        if (channel.name == GENERAL_CHANNEL_NAME) {
            myView.findViewById<Button>(R.id.channel_leave_btn).visibility = View.GONE
        }

        // show delete button if I created the channel
        if (channel.ownerId == UserService.getUserInfo()._id) {
            myView.findViewById<Button>(R.id.channel_delete_btn).visibility = View.VISIBLE
        }

        // set up Recycler View
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ChatAdapterService.getChatMsgAdapter()

        // update chat messages
        val currentChat = ChatService.getChannelMessages(channel.name) as MutableSet<ChatSocketModel>
        ChatAdapterService.getChatMsgAdapter().setChat(currentChat)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        onEnterKeyPressed(chatMsgEditText) { sendChat() }
        myView.findViewById<Button>(R.id.chat_sent_btn).setOnClickListener { sendChat() }
        myView.findViewById<LinearLayout>(R.id.chat_message_main).setOnTouchListener { _, _ -> closeKeyboard(requireActivity()) }
        myView.findViewById<Button>(R.id.channel_leave_btn).setOnClickListener { leaveRoom() }

        myView.findViewById<Button>(R.id.channel_delete_btn).setOnClickListener {
            val deleteConfirmation = DeleteChannelConfirmationBottomSheet()
            deleteConfirmation.show(parentFragmentManager, "DeleteChannelConfirmationBottomSheet")
        }

        // close keyboard when clicked on screen but allow scroll
        recyclerView.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> closeKeyboard(requireActivity())
            }
            v?.onTouchEvent(event) ?: true
        }
    }

    private fun sendChat() {
        val msg = chatMsgEditText.text.toString()
        closeKeyboard(requireActivity())

        if (msg.isEmpty()) {
            printToast(requireContext(), "Please enter a valid message")
            return
        }

        val newMessage = ChatService.createMessage(msg)
        val newMessageJSON = JSONConvertor.convertToJSON(newMessage)
        ChatSocketService.sendMessage(newMessageJSON)

        chatMsgEditText.text = null

        // scroll down if I send the message
        if (newMessage.author == UserService.getUserInfo().username) {
            scrollDown()
        }
    }

    private fun scrollDown() {
        recyclerView.scrollToPosition(ChatService.getChannelMessages(channel.name)!!.size - 1);
    }

    private fun leaveRoom() {
        TextChannelService.removeFromConnectedChannels(channel)
        ChatSocketService.leaveRoom(channel.name)

        // set current channel: 0 if only General exists, else last connected channels' position
        val connectedChannelSize = TextChannelService.getConnectedChannels().size
        val newPosition =  if (connectedChannelSize == 1) 0 else connectedChannelSize - 1
        TextChannelService.setCurrentChannelByPosition(newPosition, false)

        // update UI
        MyFragmentManager(requireActivity()).open(R.id.chat_channel_framelayout, ChatMessageBoxFragment())
        ChatAdapterService.getChannelListAdapter().notifyDataSetChanged()
    }
}