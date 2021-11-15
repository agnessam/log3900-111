package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.models.TextChannelModel

object TextChannelService {
    private var hasConnectedToGeneral = false
    private lateinit var currentChannel: TextChannelModel.AllInfo
    private var allChannels: ArrayList<TextChannelModel.AllInfo>
    private var connectedChannels: ArrayList<TextChannelModel.AllInfo>

    init {
        allChannels = arrayListOf()
        connectedChannels = arrayListOf()
    }

    fun connectToGeneral() {
        this.hasConnectedToGeneral = true
    }

    fun isConnectedToGeneral(): Boolean {
        return this.hasConnectedToGeneral
    }

    fun createNewChannel(newChannel: TextChannelModel.AllInfo) {
        this.allChannels.add(newChannel)
        this.setCurrentChannel(newChannel)
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

    fun setCurrentChannel(newChannel: TextChannelModel.AllInfo) {
        this.currentChannel = newChannel
        addToConnectedChannels()
    }

    fun setCurrentChannelByPosition(position: Int, isAllChannel: Boolean) {
        this.currentChannel = if (isAllChannel) allChannels[position] else connectedChannels[position]
        addToConnectedChannels()
    }

    fun getConnectedChannels(): ArrayList<TextChannelModel.AllInfo> {
        return connectedChannels
    }

    fun removeFromConnectedChannels(channelToRemove: TextChannelModel.AllInfo) {
        connectedChannels.remove(channelToRemove)
    }

    fun doesChannelExists(channelName: String): Boolean {
        val filteredChannel = allChannels.filter { channel -> channel.name == channelName }
        return filteredChannel.isNotEmpty()
    }

    fun refreshChannelList() {
        ChatAdapterService.getChannelListAdapter().notifyDataSetChanged()
    }

    private fun addToConnectedChannels() {
        // add to connected only if user hasn't connected yet
        if (!connectedChannels.contains(currentChannel)) {
            connectedChannels.add(currentChannel)
            ChatService.addChat(currentChannel.name)
        }
    }
}