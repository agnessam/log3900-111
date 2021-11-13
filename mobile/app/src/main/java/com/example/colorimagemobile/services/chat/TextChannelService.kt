package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.models.TextChannelModel

object TextChannelService {
    private lateinit var currentChannel: TextChannelModel.AllInfo
    private var allChannels: ArrayList<TextChannelModel.AllInfo>
    private var connectedChannels: ArrayList<TextChannelModel.AllInfo>

    init {
        allChannels = arrayListOf()
        connectedChannels = arrayListOf()
    }

    fun getChannels(): List<TextChannelModel.AllInfo> {
        return this.allChannels
    }

    fun setChannels(allChannels: ArrayList<TextChannelModel.AllInfo>){
        this.allChannels = allChannels
    }

    fun getCurrentChannel(): TextChannelModel.AllInfo {
        return currentChannel
    }

    fun setCurrentChannelByPosition(position: Int, isAllChannel: Boolean) {
        this.currentChannel = if (isAllChannel) allChannels[position] else connectedChannels[position]
        addToConnectedChannels()
    }

    fun getConnectedChannels(): ArrayList<TextChannelModel.AllInfo> {
        return connectedChannels
    }

    private fun addToConnectedChannels() {
        if (!connectedChannels.contains(currentChannel)) {
            connectedChannels.add(currentChannel)
        }
    }
}