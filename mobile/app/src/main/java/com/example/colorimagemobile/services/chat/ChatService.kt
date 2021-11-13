package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.models.ChatSocketModel
import com.example.colorimagemobile.services.UserService
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

object ChatService {
    // roomName: [messages]
    private var channelMessages: HashMap<String, MutableSet<ChatSocketModel>> = HashMap()

    fun addChat(name: String) {
        channelMessages[name] = mutableSetOf()
    }

    fun addMessage(message: ChatSocketModel) {
        channelMessages[message.roomName]!!.add(message)
    }

    fun getChannelMessages(roomName: String): MutableSet<ChatSocketModel>? {
        return channelMessages[roomName]
    }

    fun createMessage(message: String): ChatSocketModel {
        return ChatSocketModel(
            _id = null,
            message = message,
            timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
            author = UserService.getUserInfo().username,
            _roomId = TextChannelService.getCurrentChannel()._id,
            roomName = TextChannelService.getCurrentChannel().name
        )
    }
}