package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

object TextChannelService {
    private lateinit var textChannelInfo: TextChannelModel.AllInfo
    private lateinit var allTextChannel: List<TextChannelModel.AllInfo>
    private var allTextChannelName: MutableList<String> = mutableListOf()
    private var allTextChannelOwner: MutableList<String> = mutableListOf()

    fun setAllTextChannel(allInfo: List<TextChannelModel.AllInfo>){

        this.allTextChannel = allInfo
        printMsg("all channel in service data  "+ this.allTextChannel)
        setChannelsName()
    }

    fun setChannelsName(){
            for(data in allTextChannel){
                this.allTextChannelName.add(data.name)
                this.allTextChannelOwner.add(data.ownerId)
            }
        printMsg("all channel in service "+ allTextChannelName)
    }


    fun getAllTextChannelName(): MutableList<String>{
        printMsg("inside get channelname  "+ allTextChannelName)
        return this.allTextChannelName
    }

    fun setChatInfo(newTextInfo: TextChannelModel.AllInfo) {
        this.textChannelInfo = newTextInfo

    }

    fun getChatInfo(): TextChannelModel.AllInfo{
        return this.textChannelInfo
    }

}