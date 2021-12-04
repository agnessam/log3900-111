package com.example.colorimagemobile.services.chat

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.ChatSocketModel
import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.repositories.TextChannelRepository
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.socket.ChatSocketService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.ui.home.fragments.chat.ChatMessageBoxFragment
import com.example.colorimagemobile.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * based on current channel:
 * hasFetchedOldMsg: Boolean to check if we have already loaded previous messages or not
 * messages: its chat messages
 */
data class ChatMessage(var hasFetchedOldMsg: Boolean, var messages: MutableSet<ChatSocketModel>)

object ChatService {
    // roomName: [messages]
    private var channelMessages: HashMap<String, ChatMessage> = HashMap()

    // create message list for specific room
    fun addChat(name: String) {
        if (!channelMessages.containsKey(name)) {
            channelMessages[name] = ChatMessage(false, mutableSetOf())
        }
    }

    fun setMessages(roomName: String, messages: MutableSet<ChatSocketModel>) {
        channelMessages[roomName]?.messages = messages
    }

    // add message to a room
    fun addMessage(message: ChatSocketModel) {
        if (message._id != null) {
            channelMessages[message.roomName]!!.messages.add(message)
        }
    }

    // get messages of a specific room
    fun getChannelMessages(roomName: String): MutableSet<ChatSocketModel>? {
        return channelMessages[roomName]!!.messages
    }

    fun setHasFetchedMessages(channelName: String) {
        channelMessages[channelName]!!.hasFetchedOldMsg = true
    }

    fun shouldHideLoadPreviousBtn(channelName: String): Boolean {
        return channelMessages[channelName]!!.hasFetchedOldMsg
    }

    fun refreshChatBox(fragmentActivity: FragmentActivity) {
        MyFragmentManager(fragmentActivity).open(R.id.chat_channel_framelayout, ChatMessageBoxFragment())
    }

    // add previous channels to the beginning of channel
    fun addToStartOfChannel(channelName: String, oldMessages: ArrayList<ChatSocketModel>) {
        val currentMessages = channelMessages[channelName]!!.messages
        oldMessages.addAll(currentMessages)
        channelMessages[channelName]!!.messages.clear()
        channelMessages[channelName]!!.messages.addAll(oldMessages)
    }

    fun createMessage(message: String): ChatSocketModel {
        return ChatSocketModel(
            _id = null,
            message = message,
            timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
            author = UserService.getUserInfo().username,
            roomId = TextChannelService.getCurrentChannel()._id,
            roomName = TextChannelService.getCurrentChannel().name
        )
    }

    fun initChat(context: Context, callback: () -> Unit) {
        // get all public channels
        TextChannelRepository().getAllTextChannel(UserService.getToken()).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) { return@observe }
            val channels = it.data as ArrayList<TextChannelModel.AllInfo>
            TextChannelService.setPublicChannels(channels)

            channels.forEach { channel ->
                val socketInformation = Constants.SocketRoomInformation(UserService.getUserInfo()._id, channel.name)
                ChatSocketService.joinRoom(socketInformation)
            }
        })

        // get user teams and connect for each team
        UserRepository().getUserTeams(UserService.getToken(), UserService.getUserInfo()._id).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) { return@observe }

            val userTeams = it.data as List<TeamModel>
            userTeams.forEach { team ->
                val socketInformation = Constants.SocketRoomInformation(UserService.getUserInfo()._id, team.name)
                ChatSocketService.joinRoom(socketInformation)
            }

            TextChannelRepository().getTeamChannels().observe(context as LifecycleOwner, { teamChannels ->
                if (teamChannels.isError as Boolean) { return@observe }
                val channels = teamChannels.data as ArrayList<TextChannelModel.AllInfo>

                channels.forEach { channel ->
                    val isInConnectedChannels = TextChannelService.getConnectedChannels().any { connectedChannel -> connectedChannel._id == channel._id }
                    val isUserInTeam = userTeams.any { userTeam -> userTeam._id == channel.team }

                    if (isUserInTeam && !isInConnectedChannels) {
                        TextChannelService.setCurrentChannel(channels[0])
                        TextChannelService.addToConnectedChannels(channel)
                    } else if (!isUserInTeam && isInConnectedChannels){
                        TextChannelService.removeFromConnectedChannels(channel)
                    }
                }

                callback()
            })
        })
    }
}