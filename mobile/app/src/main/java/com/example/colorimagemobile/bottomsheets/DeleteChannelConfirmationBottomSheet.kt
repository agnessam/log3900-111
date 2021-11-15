package com.example.colorimagemobile.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.repositories.TextChannelRepository
import com.example.colorimagemobile.services.chat.ChatService
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteChannelConfirmationBottomSheet: BottomSheetDialogFragment() {
    private lateinit var cancelBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var dialog: BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_delete_channel_confirmation, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun closeSheet() { dialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancelBtn = view.findViewById(R.id.channelDeleteCancelBtn)
        deleteBtn = view.findViewById(R.id.channelDeleteYesBtn)

        updateUI(view)
        setListeners()
    }

    private fun updateUI(view: View) {
        val currentChannel = TextChannelService.getCurrentChannel()
        view.findViewById<TextView>(R.id.ChannelDeleteName).text = currentChannel.name
    }

    private fun setListeners() {
        cancelBtn.setOnClickListener { closeSheet() }
        deleteBtn.setOnClickListener { deleteChannel() }
    }

    private fun deleteChannel() {
        val channelRepository = TextChannelRepository()
        val currentChannel = TextChannelService.getCurrentChannel()

        channelRepository.deleteChannelById(currentChannel._id as String, currentChannel.name).observe(this, {
            closeSheet()
            printToast(requireActivity(), it.message!!)

            if (it.isError as Boolean) {
                return@observe
            }

            TextChannelService.deleteChannel(currentChannel)
            ChatService.refreshChatBox(requireActivity())
            TextChannelService.refreshChannelList()
        })
    }
}