package com.example.colorimagemobile.services.chat

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.repositories.TextChannelRepository
import com.example.colorimagemobile.services.socket.ChatSocketService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.Constants

object TextChannelService {
    private var hasConnectedToGeneral = false
    private lateinit var collaborationChannel: TextChannelModel.AllInfo
    private lateinit var currentChannel: TextChannelModel.AllInfo
    private var publicChannels: ArrayList<TextChannelModel.AllInfo>
    private var connectedChannels: ArrayList<TextChannelModel.AllInfo>

    init {
        publicChannels = arrayListOf()
        connectedChannels = arrayListOf()
    }

    fun connectToGeneral() {
        this.hasConnectedToGeneral = true
    }

    fun resetConnectedChannels() {
        this.connectedChannels = arrayListOf()
    }

    fun isConnectedToGeneral(): Boolean {
        return this.hasConnectedToGeneral
    }

    fun createNewChannel(newChannel: TextChannelModel.AllInfo) {
        if (!newChannel.isPrivate) this.publicChannels.add(newChannel)
        this.setCurrentChannel(newChannel)

        val socketInformation = Constants.SocketRoomInformation(UserService.getUserInfo()._id, newChannel.name)
        ChatSocketService.joinRoom(socketInformation)
    }

    fun getPublicChannels(): List<TextChannelModel.AllInfo> {
        return this.publicChannels
    }

    fun setPublicChannels(newChannels: ArrayList<TextChannelModel.AllInfo>){
        this.publicChannels = newChannels
    }

    fun getCurrentChannel(): TextChannelModel.AllInfo {
        return currentChannel
    }

    fun setCurrentChannel(newChannel: TextChannelModel.AllInfo) {
        this.currentChannel = newChannel
        addToConnectedChannels(this.currentChannel)
    }

    fun isConnectedChannelInitialized(): Boolean {
        return this::currentChannel.isInitialized
    }

    fun setCurrentChannelByPosition(position: Int, isAllChannel: Boolean) {
        this.currentChannel = if (isAllChannel) this.publicChannels[position] else connectedChannels[position]
        addToConnectedChannels(this.currentChannel)
    }

    fun setCollaborationChannel(channel: TextChannelModel.AllInfo) {
        this.collaborationChannel = channel
    }

    fun getCollaborationChannel(): TextChannelModel.AllInfo {
        return this.collaborationChannel
    }

    fun getConnectedChannels(): ArrayList<TextChannelModel.AllInfo> {
        return connectedChannels
    }

    fun removeFromConnectedChannels(channelToRemove: TextChannelModel.AllInfo) {
        connectedChannels.remove(channelToRemove)
        val socketInformation = Constants.SocketRoomInformation(UserService.getUserInfo()._id, channelToRemove.name)
        ChatSocketService.leaveRoom(socketInformation)
        this.currentChannel = publicChannels[0]
    }

    fun deleteChannel(channelToDelete: TextChannelModel.AllInfo) {
        val socketInformation = Constants.SocketRoomInformation(UserService.getUserInfo()._id, channelToDelete.name)
        ChatSocketService.leaveRoom(socketInformation)
        this.publicChannels.remove(channelToDelete)
        removeFromConnectedChannels(channelToDelete)
        updateCurrentChannel()
    }

    fun removeChannel(channelName: String?) {
        if (channelName.isNullOrEmpty()) return

        this.connectedChannels.removeIf { it.name == channelName }
        this.currentChannel = publicChannels[0]
    }

    fun doesChannelExists(channelName: String): Boolean {
        val filteredChannel = this.publicChannels.filter { channel -> channel.name == channelName }
        return filteredChannel.isNotEmpty()
    }

    fun refreshChannelList() {
        ChatAdapterService.getChannelListAdapter().notifyDataSetChanged()
    }

    private fun updateCurrentChannel() {
        // set current channel: 0 if only General exists, else last connected channels' position
        val newPosition =  if (connectedChannels.size == 1) 0 else connectedChannels.size - 1
        setCurrentChannelByPosition(newPosition, false)
    }

    fun addToConnectedChannels(channel: TextChannelModel.AllInfo) {
        // add to connected only if user hasn't connected yet
        if (!connectedChannels.contains(channel)) {
            connectedChannels.add(channel)
            ChatService.addChat(channel.name)
        }
    }

    fun createChannel(channelModel: TextChannelModel.AllInfo, context: Context, callback: () -> Unit) {
        TextChannelRepository().addChannel(channelModel).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                return@observe
            }

            val channel = it.data as TextChannelModel.AllInfo
            createNewChannel(channel)
            callback()
        })
    }
}