package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.models.MessageModel

object MessageService {
    private lateinit var message: MessageModel.MessageAllInfo

    fun setChatInfo(newMessage: MessageModel.MessageAllInfo) {
        this.message = newMessage
    }

    fun getChatInfo(): MessageModel.MessageAllInfo{
        return this.message
    }

    fun isNull(): Boolean {
        return !MessageService::message.isInitialized
    }
}