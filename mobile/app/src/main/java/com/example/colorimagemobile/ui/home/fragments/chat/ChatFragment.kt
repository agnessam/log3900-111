package com.example.colorimagemobile.ui.home.fragments.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyFragmentManager

class ChatFragment : Fragment(R.layout.fragment_chat) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyFragmentManager(requireActivity()).open(R.id.chatChannelFragment, ChannelNameFragment())
    }
}