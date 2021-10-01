package com.example.colorimagemobile.model

class Message(message: String, timestamp: String, author: String, roomName: String) {
    var message: String = ""
    var timestamp: String = ""
    var author: String = ""
    var roomName: String = ""

    init {
        this.message = message
        this.timestamp = timestamp
        this.author = author
        this.roomName = roomName
    }
}