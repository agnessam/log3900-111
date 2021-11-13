package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.models.MessageModel

object MessageService {
    private lateinit var message: MessageModel.AllInfo

    fun setChatInfo(newMessage: MessageModel.AllInfo) {
        this.message = newMessage
    }

    fun getChatInfo(): MessageModel.AllInfo{
        return this.message
    }

    fun isNull(): Boolean {
        return !MessageService::message.isInitialized
    }
}