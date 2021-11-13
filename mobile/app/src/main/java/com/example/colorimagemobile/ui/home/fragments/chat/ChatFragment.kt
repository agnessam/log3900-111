package com.example.colorimagemobile.ui.home.fragments.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.ChatChannelAdapter
import com.example.colorimagemobile.classes.MyFragmentManager
import com.google.android.material.tabs.TabLayout

class ChatFragment : Fragment(R.layout.fragment_chat) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setChannelTabs(view)
        MyFragmentManager(requireActivity()).open(R.id.chat_channel_framelayout, ChatWelcomeFragment())
    }

    private fun setChannelTabs(view: View) {
        val tabLayout = view.findViewById<TabLayout>(R.id.chat_channel_tablayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.chat_view_pager)

        viewPager.adapter = ChatChannelAdapter(parentFragmentManager, requireActivity().lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("All"))
        tabLayout.addTab(tabLayout.newTab().setText("Connected"))

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }
            override fun onTabReselected(tab: TabLayout.Tab?) { }
            override fun onTabUnselected(tab: TabLayout.Tab?) { }
        })

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}