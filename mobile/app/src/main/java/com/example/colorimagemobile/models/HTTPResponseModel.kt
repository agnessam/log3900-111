package com.example.colorimagemobile.models

// model class for API Response
class HTTPResponseModel {
    data class LoginResponse(val user: UserModel.AllInfo, val token: String, val info: String, val error: String)
    data class RegisterResponse(val user: UserModel.AllInfo, val token: String, val info: String, val error: String)

    data class GetUser(val user: UserModel.AllInfo, val err: String)
    data class UpdateUser(val user: UserModel.AllInfo, val err: String)

    data class GetChatChannel(val channelInfo : TextChannelModel.TextChannelAllInfo, val err : String)
    data class DeleteChatChannel(val channelInfo : TextChannelModel.TextChannelAllInfo, val err : String )

    data class GetChatMessage(val chatMessage : MessageModel.MessageAllInfo , val err : String )
    data class GetChatMessageHistory (val channelName : String, val chatMessage : MessageModel.MessageAllInfo , val err : String )
}
