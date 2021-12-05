package com.example.colorimagemobile.ui.home.fragments.accountParameter

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyFragmentManager

class CollaborationHistoryDrawing : Fragment(R.layout.fragment_collaboration_history_drawing) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyFragmentManager(requireActivity()).open(R.id.collaborationFrameLayout, HistoryFragment())
    }


}