package com.example.colorimagemobile.services.chat

import com.example.colorimagemobile.adapter.ChatMessageRecyclerAdapter

object ChatAdapterService {
    private var adapter: ChatMessageRecyclerAdapter = ChatMessageRecyclerAdapter()

    fun getAdapter(): ChatMessageRecyclerAdapter {
        return adapter
    }
}