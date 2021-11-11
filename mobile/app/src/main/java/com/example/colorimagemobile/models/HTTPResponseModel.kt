package com.example.colorimagemobile.models

// model class for API Response
class HTTPResponseModel {
    data class LoginResponse(val user: UserModel.AllInfo, val token: String, val info: String, val error: String)
    data class RegisterResponse(val user: UserModel.AllInfo, val token: String, val info: String, val error: String)

    data class UserResponse(val user: UserModel.AllInfo, val err: String)

    data class TextChannelResponse(val channelInfo : ChatChannelModel.AllInfo, val err : String )

    data class MessageResponse(val chatMessage : MessageModel.MessageAllInfo, val err : String )
    data class ChatMessageHistory (val channelName : String, val chatMessage : MessageModel.MessageAllInfo , val err : String )
}
