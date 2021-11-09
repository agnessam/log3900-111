package com.example.colorimagemobile.interfaces

interface ICommand {
    // object which we receive from socket
    fun update(drawingCommand: SyncUpdate)
    fun execute()
}