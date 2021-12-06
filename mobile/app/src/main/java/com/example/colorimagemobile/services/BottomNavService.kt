package com.example.colorimagemobile.services

import com.example.colorimagemobile.R
import com.google.android.material.bottomnavigation.BottomNavigationView

object BottomNavService {

    private lateinit var bottomNav: BottomNavigationView

    fun setNav(nav: BottomNavigationView) {
        this.bottomNav = nav
    }

    // won't be using it but still keeping it
    fun getView(): Int {
        val currentView = bottomNav.menu.findItem(bottomNav.selectedItemId)

        if (currentView.title == "Chat") {
            bottomNav.selectedItemId = R.id.chatFragment
        }

        return when (currentView.title) {
            "Gallery" -> R.id.main_gallery_fragment
            "Teams" -> R.id.teamsMenuFrameLayout
            "Chat" -> R.id.chatChannelFragment
            else -> R.id.main_gallery_fragment
        }
    }
}