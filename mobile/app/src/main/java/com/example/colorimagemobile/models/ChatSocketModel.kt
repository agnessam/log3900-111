package com.example.colorimagemobile.models

data class ChatSocketModel(
    val _id: String?,
    val message: String,
    val timestamp: String,
    val author: String,
    val roomId: String?,
    val roomName: String
)