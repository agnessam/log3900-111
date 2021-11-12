package com.example.colorimagemobile.models

import com.google.gson.annotations.SerializedName

class TextChannelModel {
    data class CreateChatChannel(var nom: String,var owner:String)

    data class AllInfo(
        @SerializedName("_id")
        var _id : String,

        @SerializedName("name")
        var name: String,

        @SerializedName("ownerId")
        var ownerId: String
    )
}