package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.models.ChatSocketModel

object ChatService {
    private lateinit var currentChat: ChatSocketModel
    private var chatMessages: ArrayList<ChatSocketModel> = arrayListOf()

    fun getMessages(): ArrayList<ChatSocketModel> {
        return chatMessages
    }

    fun setMessages(newChat: ArrayList<ChatSocketModel>) {
        this.chatMessages = newChat
    }

    fun addMessage(message: ChatSocketModel) {
        chatMessages.add(message)
    }
}