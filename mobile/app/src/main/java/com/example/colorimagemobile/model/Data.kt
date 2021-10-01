package com.example.colorimagemobile.ui

data class MessageWithType(val message : String, val timestamp: String, val author: String, val roomName: String, var viewType: Int)

data class Message(val message : String, val timestamp: String, val author: String, val roomName: String)