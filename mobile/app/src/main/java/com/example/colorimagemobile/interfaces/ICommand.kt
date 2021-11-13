package com.example.colorimagemobile.interfaces

import com.example.colorimagemobile.models.SyncUpdate

interface ICommand {
    // object which we receive from socket
    fun update(drawingCommand: SyncUpdate)
    fun execute()
}