package com.example.colorimagemobile.ui.home.fragments.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.bottomsheets.ConfirmationBottomSheet
import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.enumerators.ButtonType
import com.example.colorimagemobile.models.ChatSocketModel
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.repositories.TextChannelRepository
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.services.chat.ChatAdapterService
import com.example.colorimagemobile.services.chat.ChatService
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.services.socket.ChatSocketService
import com.example.colorimagemobile.utils.CommonFun.Companion.hideKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.onEnterKeyPressed
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.Constants.Companion.GENERAL_CHANNEL_NAME
import kotlinx.android.synthetic.main.fragment_chat_message_box.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

class ChatMessageBoxFragment : Fragment(R.layout.fragment_chat_message_box) {

    private lateinit var myView: View
    private lateinit var channel: TextChannelModel.AllInfo
    private lateinit var chatMsgEditText: EditText
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view
        chatMsgEditText = myView.findViewById(R.id.chat_text_input)
        recyclerView = myView.findViewById(R.id.chat_message_recycler_view)

        if (!TextChannelService.isConnectedChannelInitialized()) {
            myView.findViewById<RelativeLayout>(R.id.chat_box_container).visibility = View.GONE
            return
        }

        myView.findViewById<RelativeLayout>(R.id.chat_box_container).visibility = View.VISIBLE
        channel = TextChannelService.getCurrentChannel()
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

    private fun updateUI() {
        myView.findViewById<TextView>(R.id.chat_username).text = channel.name

        if (channel.name == GENERAL_CHANNEL_NAME) {
            myView.findViewById<Button>(R.id.channel_leave_btn).visibility = View.GONE
            myView.findViewById<Button>(R.id.channel_delete_btn).visibility = View.GONE
           // hideLoadPreviousBtn()
        }

        // show delete button if I created the channel
        if (channel.ownerId == UserService.getUserInfo()._id) {
            myView.findViewById<Button>(R.id.channel_delete_btn).visibility = View.VISIBLE
        }

        // remove leaveRoom button for General
        if (channel.isPrivate) {
            myView.findViewById<Button>(R.id.channel_leave_btn).visibility = View.GONE
            myView.findViewById<Button>(R.id.channel_delete_btn).visibility = View.GONE
        }

        // show leave if channel is public and connected
        if (!channel.isPrivate && TextChannelService.getConnectedChannels().contains(channel)) {
            myView.findViewById<Button>(R.id.channel_leave_btn).visibility = View.VISIBLE
        }

        // hide Load Previous Messages button if we have already loaded old messages
        if (ChatService.shouldHideLoadPreviousBtn(channel.name)) {
            hideLoadPreviousBtn()
        }

        // set up Recycler View
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ChatAdapterService.getChatMsgAdapter()

        // update chat messages
        var currentChat = filterChatMessage()!!
        ChatAdapterService.getChatMsgAdapter().setChat(currentChat)
        scrollDown()
    }

    private fun filterChatMessage(): MutableSet<ChatSocketModel>? {
        if (!ChatService.containsChannel(channel.name)) return mutableSetOf()
        if (UserService.getUserInfo().lastLogin == null) ChatService.getChannelMessages(channel.name)?.toMutableSet()

        val messageObj = ChatService.getChannelMessageObject(channel.name)
        if (!messageObj.hasFetchedOldMsg) {

            val loginTimeMsg = messageObj.messages.indexOfLast { msg ->
                Instant.parse(msg.timestamp) >= UserService.getUserInfo().lastLogin?.toInstant()
            }

            if (loginTimeMsg == 0) {
                ChatService.setHasFetchedMessages(channel.name)
                hideLoadPreviousBtn()
            }

            if (loginTimeMsg == -1) return mutableSetOf() else  ChatService.getChannelMessages(channel.name)?.filterIndexed { index, _ -> index > loginTimeMsg }?.toMutableSet()
        }

        return ChatService.getChannelMessages(channel.name)?.toMutableSet()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        onEnterKeyPressed(chatMsgEditText) { sendChat() }
        myView.findViewById<Button>(R.id.chat_sent_btn).setOnClickListener { hideKeyboard(requireContext(),chat_message_main); sendChat() }
        myView.findViewById<LinearLayout>(R.id.chat_message_main).setOnTouchListener { _, _ -> hideKeyboard(requireContext(),chat_message_main) }
        myView.findViewById<Button>(R.id.channel_leave_btn).setOnClickListener { leaveRoom() }
        myView.findViewById<Button>(R.id.channel_load_more_btn).setOnClickListener { loadPreviousMessages() }

        myView.findViewById<Button>(R.id.channel_delete_btn).setOnClickListener {
            val title = "Delete channel confirmation"
            val description = "Are you sure you want to delete the following channel?\n${TextChannelService.getCurrentChannel().name}"
            val deleteConfirmation = ConfirmationBottomSheet({deleteChannel()}, title, description, "DELETE", ButtonType.DELETE.toString())
            deleteConfirmation.show(parentFragmentManager, "ConfirmationBottomSheet")
        }

        // close keyboard when clicked on screen but allow scroll
        recyclerView.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> hideKeyboard(requireContext(),recyclerView)
            }
            v?.onTouchEvent(event) ?: true
        }
    }

    private fun deleteChannel() {
        val currentChannel = TextChannelService.getCurrentChannel()

        TextChannelService.deleteChannel(currentChannel)
        ChatService.refreshChatBox(requireActivity())
        TextChannelService.refreshChannelList()

        TextChannelRepository().deleteChannelById(currentChannel._id as String, currentChannel.name).observe(context as LifecycleOwner, {
            printToast(requireActivity(), it.message!!)
        })

        TextChannelRepository().deleteMessages(currentChannel._id!!).observe(context as LifecycleOwner, { })
    }

    private fun sendChat() {
        val msg = chatMsgEditText.text.toString()
//        closeKeyboard(requireActivity())

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

    private fun loadPreviousMessages() {
        TextChannelRepository().getTextChannelMessages(channel._id!!).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                printToast(requireActivity(), it.message!!)
                return@observe
            }

            ChatService.setHasFetchedMessages(channel.name)
            hideLoadPreviousBtn()

            val oldChannels = it.data as ArrayList<ChatSocketModel>
            ChatService.addToStartOfChannel(channel.name, oldChannels)
            ChatService.refreshChatBox(requireActivity())
        })
    }

    private fun hideLoadPreviousBtn() {
        myView.findViewById<Button>(R.id.channel_load_more_btn).visibility = View.GONE
    }

    private fun leaveRoom() {
        TextChannelService.removeFromConnectedChannels(channel)

        // update UI
        ChatService.refreshChatBox(requireActivity())
        TextChannelService.refreshChannelList()
    }
}