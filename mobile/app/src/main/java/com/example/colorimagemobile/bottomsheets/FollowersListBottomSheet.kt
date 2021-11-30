package com.example.colorimagemobile.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.FollowersListRecyclerAdapter
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_followers_list_bottom_sheet.*

class FollowersListBottomSheet : BottomSheetDialogFragment() , FollowersListRecyclerAdapter.OnItemClickListener{
    private lateinit  var layoutManagerFollower : RecyclerView.LayoutManager
    private lateinit  var adapterFollower: RecyclerView.Adapter<FollowersListRecyclerAdapter.ViewHolder>
    private lateinit var dialog: BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun closeSheet() { dialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers_list_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManagerFollower = GridLayoutManager(context,Constants.NB_DATA_ROWS_TWO)
        recyclerViewFollowers.layoutManager = layoutManagerFollower
        adapterFollower = FollowersListRecyclerAdapter(this)
        recyclerViewFollowers.adapter = adapterFollower

    }
    override fun onItemClick(position: Int) {
        closeSheet()

    }
}