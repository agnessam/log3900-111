package com.example.colorimagemobile.ui.home.fragments.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.ChannelListRecyclerAdapter
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.repositories.TextChannelRepository
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.services.chat.TextChannelService

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var channels: List<TextChannelModel.AllInfo>
    private lateinit var myView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view
        getAllChannels(UserService.getToken())
        MyFragmentManager(requireActivity()).open(R.id.chat_channel_framelayout, ChatWelcomeFragment())
    }

    private fun getAllChannels(token: String) {
        TextChannelRepository().getAllTextChannel(token).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                return@observe
            }

            channels = it.data as List<TextChannelModel.AllInfo>
            TextChannelService.setChannels(channels)
            setRecyclerView()
        })
    }

    private fun setRecyclerView() {
        val recyclerView = myView.findViewById<RecyclerView>(R.id.ChannelRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ChannelListRecyclerAdapter()
    }
}