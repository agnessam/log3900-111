package com.example.colorimagemobile.ui.home.fragments.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast

class ChatMessageBoxFragment : Fragment(R.layout.fragment_chat_message_box) {

    private lateinit var myView: View
    private lateinit var channel: TextChannelModel.AllInfo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view
        channel = TextChannelService.getCurrentChannel()

        updateUI()
    }

    private fun updateUI() {
        myView.findViewById<TextView>(R.id.chat_username).text = channel.name
    }

    override fun onStop() {
        super.onStop()
        closeSockets()
    }

    override fun onDestroy() {
        super.onDestroy()
        closeSockets()
    }

    // leave sockets here
    private fun closeSockets() {
        printMsg("Leave ${channel.name}")
    }
}