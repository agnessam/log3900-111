package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.models.TextChannelModel

object TextChannelService {
    private lateinit var textChannelInfo: TextChannelModel.AllInfo
    private lateinit var allTextChannel: List<TextChannelModel.AllInfo>
    private var allTextChannelName: MutableList<String> = mutableListOf()
    private var allTextChannelOwner: MutableList<String> = mutableListOf()

    fun setAllTextChannel(allInfo: List<TextChannelModel.AllInfo>){
        this.allTextChannel = allInfo
        setChannelsName()
    }

    fun getAllTextChannelName(): MutableList<String>{
        return this.allTextChannelName
    }

    fun setChannelsName(){
            for(data in allTextChannel){
                this.allTextChannelName.add(data.name)
                this.allTextChannelOwner.add(data.ownerId)
            }
    }

    fun setChatInfo(newTextInfo: TextChannelModel.AllInfo) {
        this.textChannelInfo = newTextInfo

    }

    fun getChatInfo(): TextChannelModel.AllInfo{
        return this.textChannelInfo
    }

}