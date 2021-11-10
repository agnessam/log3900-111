package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.models.ChatChannelModel
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

object ChatChannelService {
    private lateinit var chatInfo: ChatChannelModel.TextChannelInfo
    private lateinit var  allChatInfo: List<ChatChannelModel.TextChannelInfo>
    private lateinit var data : List<HTTPResponseModel.GetChannelList>
    private  lateinit var content : ChatChannelModel.TextChannelInfo
    private lateinit var res : List<ChatChannelModel.TextChannelInfo>
   var listName : Array<String> = arrayOf()

    fun setChatInfo(newChatInfo: ChatChannelModel.TextChannelInfo) {
        this.chatInfo = newChatInfo
    }

    fun getChatInfo(): ChatChannelModel.TextChannelInfo {
        return this.chatInfo
    }

    fun isNull(): Boolean {
        return !ChatChannelService::chatInfo.isInitialized
    }

    fun setAllChatInfo(allChatInfoResponse: List<HTTPResponseModel.GetChannelList>){
      data = allChatInfoResponse
        printMsg("allChatInfoResponse with channel and err ==== "+data)
        for (post in data){
            content = post.channelInfo
//            content._id = post.channelInfo._id.toString()
//            content.nom = post.channelInfo.nom.toString()
//            content.owner = post.channelInfo.owner.toString()
        }
        for (data in allChatInfoResponse){
            res = listOf(data.channelInfo)
            printMsg("data.channeInfo === "+data.channelInfo)
            printMsg("res================ "+res)
            printMsg("all name  ==== "+data.channelInfo.nom)
            listName = arrayOf(data.channelInfo.nom)
            printMsg("listname in setAllChat "+ listName)
        }
        setAllChatName()


        printMsg("all chat info channel info  content list of data.channelinfo"+ content)

        allChatInfo = res

        printMsg("all chat info channel info "+ allChatInfo)

    }

    fun setAllChatName(){
        for (r in res.indices){
            listName[r] = res[r].nom
            printMsg("list des nom de channel "+ listName[r])
        }
        printMsg("list des nom de channel complet"+ listName)



    }

    fun getAllChatInfo(): List<ChatChannelModel.TextChannelInfo> {
        return this.allChatInfo
    }

    fun getAllChatInfoName(): Array<String>{
        return this.listName
    }
}