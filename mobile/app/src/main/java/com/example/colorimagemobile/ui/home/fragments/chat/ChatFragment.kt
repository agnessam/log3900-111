package com.example.colorimagemobile.ui.home.fragments.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.ChannelsRecyclerAdapter
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.repositories.TextChannelRepository
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.ui.home.fragments.chat.chatBox.ChatWelcomeFragment

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var myView: View
    private lateinit var adapter: ChannelsRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view
        adapter = ChannelsRecyclerAdapter()

        getAllChannels()
        MyFragmentManager(requireActivity()).open(R.id.chat_channel_framelayout, ChatWelcomeFragment())
    }

    private fun getAllChannels() {
        TextChannelRepository().getAllTextChannel(UserService.getToken()).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                return@observe
            }

            val channels = it.data as ArrayList<TextChannelModel.AllInfo>
            TextChannelService.setChannels(channels)
            setRecyclerView()
        })
    }

    private fun setRecyclerView() {
        val recyclerView = myView.findViewById<RecyclerView>(R.id.ChannelRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        showAllChannels()
        myView.findViewById<Button>(R.id.chat_connected_channels_btn).setOnClickListener { showConnectedChannels() }
        myView.findViewById<Button>(R.id.chat_all_channels_btn).setOnClickListener { showAllChannels() }
    }

    private fun showAllChannels() {
        adapter.setData(TextChannelService.getChannels() as ArrayList<TextChannelModel.AllInfo>)
    }

    private fun showConnectedChannels() {
        adapter.setData(TextChannelService.getConnectedChannels())
    }
}