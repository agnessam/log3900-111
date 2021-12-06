package com.example.colorimagemobile.ui.home.fragments.chat

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.ChannelsRecyclerAdapter
import com.example.colorimagemobile.bottomsheets.NewChannelBottomSheet
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.repositories.TextChannelRepository
import com.example.colorimagemobile.services.chat.ChatAdapterService
import com.example.colorimagemobile.services.chat.ChatService
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.utils.Constants.Companion.GENERAL_CHANNEL_NAME

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var myView: View
    private lateinit var searchView: SearchView
    private lateinit var adapter: ChannelsRecyclerAdapter
    private lateinit var searchNoResult: TextView
    private var isSearching = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        adapter = ChatAdapterService.getChannelListAdapter()
        setSearchIcon(view)

        ChatService.initChat(requireContext()) { init() }
    }

    private fun init() {
        setRecyclerView()
        setButtonListeners()

        // means its not the first time we are opening the chat
        if (TextChannelService.getPublicChannels().isNotEmpty()) {
            ChatService.refreshChatBox(requireActivity())
        }
    }

    private fun setRecyclerView() {
        val recyclerView = myView.findViewById<RecyclerView>(R.id.ChannelRecycleView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun setButtonListeners() {
        val connectedBtn = myView.findViewById<Button>(R.id.chat_connected_channels_btn)
        val allBtn = myView.findViewById<Button>(R.id.chat_all_channels_btn)
        val createChannelBtn = myView.findViewById<Button>(R.id.channel_add_btn)

        showAllChannels()
        changeBtnColor(allBtn, connectedBtn)

        allBtn.setOnClickListener {
            showAllChannels()
            changeBtnColor(allBtn, connectedBtn)
            myView.findViewById<SearchView>(R.id.searchChannelsIcon).visibility = View.VISIBLE
        }

        connectedBtn.setOnClickListener {
            showConnectedChannels()
            changeBtnColor(connectedBtn, allBtn)
            myView.findViewById<SearchView>(R.id.searchChannelsIcon).visibility = View.GONE
        }

        createChannelBtn.setOnClickListener {
            val newChannelMenu = NewChannelBottomSheet()
            newChannelMenu.show(parentFragmentManager, "NewChannelBottomSheet")
        }
    }

    private fun showAllChannels() {
        adapter.setData(TextChannelService.getPublicChannels() as ArrayList<TextChannelModel.AllInfo>)
        adapter.setIsAllChannels(true)
        adapter.setUnreadChannels(ArrayList())
    }

    private fun showConnectedChannels() {
        adapter.setData(TextChannelService.getConnectedChannels())
        adapter.setIsAllChannels(false)
        adapter.setUnreadChannels(ChatService.unreadChannels)
    }

    private fun changeBtnColor(selectedBtn: Button, normalBtn: Button) {
        selectedBtn.setTextColor(Color.parseColor("#4050b5"))
        normalBtn.setTextColor(Color.parseColor("#888888"))
    }

    // add channel named General as connected! usually its the first channel in the list
    private fun addDefaultChannel(channels: ArrayList<TextChannelModel.AllInfo>) {
        if (TextChannelService.isConnectedToGeneral()) return

        channels.forEach {
            if (it.name == GENERAL_CHANNEL_NAME) {
                TextChannelService.setCurrentChannel(it)
                TextChannelService.connectToGeneral()
                ChatService.refreshChatBox(requireActivity())
                return
            }
        }
    }

    private fun setSearchIcon(view: View) {
        searchView = view.findViewById(R.id.searchChannelsIcon)
        searchView.queryHint = "Search channels"
        searchNoResult = view.findViewById(R.id.searchNotFoundText) as TextView
        searchNoResult.visibility = View.GONE

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNullOrEmpty()) {
                    TextChannelService.clearSearch()
                    if (isSearching) {
                        TextChannelService.setPublicChannels(TextChannelService.getBackedUpChannels())
                        TextChannelService.setBackedUpChannels(ArrayList())
                        showAllChannels()
                    }
                    isSearching = false
                    searchNoResult.visibility = View.GONE
                }
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                TextChannelService.setSearchQuery(query)
                searchView.clearFocus()
                filterData()
                isSearching = true
                return true
            }
        })
    }

    private fun filterData() {
        val query = TextChannelService.getSearchQuery()
        if (query.isNullOrEmpty()) return

        TextChannelRepository().searchChannels(query).observe(this, {
            if (it.isError as Boolean) { return@observe }

            // Prevents back up to get overwritten again by accidnet
            if (it.data != TextChannelService.getPublicChannels()) {
                searchNoResult.visibility = View.GONE
                TextChannelService.setBackedUpChannels(TextChannelService.getPublicChannels() as ArrayList<TextChannelModel.AllInfo>)
                TextChannelService.setPublicChannels(it.data as ArrayList<TextChannelModel.AllInfo>)
                showAllChannels()
            }

            if (it.data.isNullOrEmpty()) {
                searchNoResult.visibility = View.VISIBLE
            }

        })
    }
}