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

        deleteBtn.setOnClickListener {

        }
    }

//    private fun createChannel(newChannelModel: TextChannelModel.AllInfo) {
//        val channelRepository = TextChannelRepository()
//
//        channelRepository.addChannel(newChannelModel).observe(this, {
//            closeSheet()
//
//            if (it.isError as Boolean) {
//                printToast(requireActivity(), it.message!!)
//                return@observe
//            }
//
//            val channel = it.data as TextChannelModel.AllInfo
//            TextChannelService.createNewChannel(channel)
//            TextChannelService.refreshChannelList()
//            ChatService.refreshChatBox(context as FragmentActivity)
//        })
//    }
}