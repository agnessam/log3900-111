package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.models.TextChannelModel

object TextChannelService {
    private lateinit var currentChannel: TextChannelModel.AllInfo
    private lateinit var allChannels: List<TextChannelModel.AllInfo>

    init {
        allChannels = arrayListOf()
    }

    fun getChannels(): List<TextChannelModel.AllInfo> {
        return this.allChannels
    }

    fun setChannels(allChannels: List<TextChannelModel.AllInfo>){
        this.allChannels = allChannels
    }

    fun getCurrentChannel(): TextChannelModel.AllInfo {
        return currentChannel
    }

    fun setCurrentChannelByPosition(position: Int) {
        this.currentChannel = allChannels[position]
    }
}