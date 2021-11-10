package com.example.colorimagemobile.models

class ChatChannelModel {
    data class CreateChatChannel(var nom: String,var owner:String)

    data class TextChannelInfo(
        var _id : String,
        var nom: String,
        var owner: String
    )
}