package com.example.colorimagemobile.models

class MessageModel {
    data class SendMessage(var message: String,var timestamp: String,var author: String,var roomId: String,var roomName: String)

    data class MessageAllInfo (
        var _id : String?,
        var message: String,
        var timestamp: String,
        var author: String,
        var roomId: String,
        var roomName: String,
    )

}