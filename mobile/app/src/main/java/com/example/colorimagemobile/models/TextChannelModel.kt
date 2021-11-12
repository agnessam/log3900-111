package com.example.colorimagemobile.models

import com.google.gson.annotations.SerializedName

class TextChannelModel {
    data class CreateChannel(var name: String, var ownerId:String)

    data class AllInfo(
        @SerializedName("_id")
        var _id : String,

        @SerializedName("name")
        var name: String,

        @SerializedName("ownerId")
        var ownerId: String
    )
}