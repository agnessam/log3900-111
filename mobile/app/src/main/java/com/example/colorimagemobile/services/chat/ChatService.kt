package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.models.ChatSocketModel

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
}