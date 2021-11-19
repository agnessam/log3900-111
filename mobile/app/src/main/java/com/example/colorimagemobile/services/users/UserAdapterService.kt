package com.example.colorimagemobile.services.users

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.adapter.UsersMenuRecyclerAdapter

object UserAdapterService {

    private lateinit var usersMenuAdapter: UsersMenuRecyclerAdapter

    fun createAdapter(context: Context, fragmentActivity: FragmentActivity, layoutID: Int, parentFragmentID: Int): UsersMenuRecyclerAdapter {
        return UsersMenuRecyclerAdapter(layoutID) { pos -> openUser(fragmentActivity, pos, parentFragmentID) }
    }

    fun setAdapter(adapter: UsersMenuRecyclerAdapter) {
        usersMenuAdapter = adapter
    }

    private fun openUser(fragmentActivity: FragmentActivity, position: Int, parentFragmentID: Int) { }
}