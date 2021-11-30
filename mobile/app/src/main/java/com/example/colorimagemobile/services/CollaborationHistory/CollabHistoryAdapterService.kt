package com.example.colorimagemobile.services.CollaborationHistory

import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.adapter.CollaborationHistoryRecyclerAdapter
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.ui.home.fragments.gallery.GalleryDrawingFragment
import com.example.colorimagemobile.utils.Constants

object CollabHistoryAdapterService {
    private lateinit var CollaborationHistoryDrawingAdapter: CollaborationHistoryRecyclerAdapter

    fun createAdapter(fragmentActivity: FragmentActivity, layoutID: Int, parentFragmentID: Int): CollaborationHistoryRecyclerAdapter {
        return CollaborationHistoryRecyclerAdapter(
            layoutID
        ) { pos -> openDrawing(fragmentActivity, pos, parentFragmentID) }
    }

    fun setAdapter(adapter: CollaborationHistoryRecyclerAdapter) {
        CollaborationHistoryDrawingAdapter = adapter
    }

    private fun openDrawing(fragmentActivity: FragmentActivity, position: Int, parentFragmentID: Int) {
        MyFragmentManager(fragmentActivity).openWithData(parentFragmentID, GalleryDrawingFragment(), Constants.DRAWINGS.CURRENT_DRAWING_ID_KEY, position)
    }
}