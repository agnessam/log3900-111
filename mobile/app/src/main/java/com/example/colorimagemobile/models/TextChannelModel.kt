package com.example.colorimagemobile.models

class TextChannelModel {
    data class CreateTextChannel(var nom: String,var owner:String)
    data class TextChannelAllInfo(
        var _id : String?,
        var nom: String,
        var owner: String
    )
}