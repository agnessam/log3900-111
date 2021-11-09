package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.models.TextChannelModel

object ChatChannelService {
    private lateinit var chatInfo: TextChannelModel.TextChannelAllInfo

    fun setChatInfo(newChatInfo: TextChannelModel.TextChannelAllInfo) {
        this.chatInfo = newChatInfo
    }

    fun getChatInfo(): TextChannelModel.TextChannelAllInfo {
        return this.chatInfo
    }

    fun isNull(): Boolean {
        return !ChatChannelService::chatInfo.isInitialized
    }
}