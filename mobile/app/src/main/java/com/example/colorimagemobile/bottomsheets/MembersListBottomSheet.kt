package com.example.colorimagemobile.bottomsheets

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.MembersListRecyclerAdapter
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.utils.CommonFun.Companion.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottomsheet_members_list.*

class MembersListBottomSheet(
    private var activity: Activity,
    private val members: ArrayList<UserModel.AllInfo>,
    private val userStatus: HashMap<String, UserModel.STATUS>
): BottomSheetDialogFragment() {
    private lateinit var dialog: BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_members_list, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun closeSheet() { dialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        membersListRecyclerView.layoutManager = LinearLayoutManager(view.context)
        membersListRecyclerView.adapter = MembersListRecyclerAdapter(requireActivity(), members, userStatus) { closeSheet() }
        setListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        membersListForm.setOnTouchListener{_, _ -> hideKeyboard(activity, membersListForm) }
    }
}