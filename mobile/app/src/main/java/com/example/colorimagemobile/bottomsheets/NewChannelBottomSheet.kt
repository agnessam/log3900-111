package com.example.colorimagemobile.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.repositories.TextChannelRepository
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.services.chat.ChatService
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.CommonFun.Companion.toggleButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NewChannelBottomSheet: BottomSheetDialogFragment() {
    private lateinit var createChannelBtn: Button
    private lateinit var channelLayout: TextInputLayout
    private lateinit var channelInputName: TextInputEditText
    private lateinit var dialog: BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_create_channel, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun closeSheet() { dialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createChannelBtn = view.findViewById(R.id.createChannelBtn)
        channelLayout = view.findViewById(R.id.newChannelInputLayout)
        channelInputName = view.findViewById(R.id.newChannelInputText)

        toggleButton(createChannelBtn, false)
        setListeners(view)
    }

    private fun setListeners(view: View) {
        channelInputName.doOnTextChanged { text, _, _, _ ->
            channelLayout.error = ""

            if (text.isNullOrEmpty()) {
                channelLayout.error = "The name can not be empty"
                updateCreateBtn()
                return@doOnTextChanged
            }

            if (TextChannelService.doesChannelExists(text.toString())) {
                channelLayout.error = "This channel already exists"
                updateCreateBtn()
                return@doOnTextChanged
            }

            updateCreateBtn()
        }

        createChannelBtn.setOnClickListener {
            printMsg(channelInputName.text.toString())
            val newChannel = TextChannelModel.AllInfo(_id = null, name = channelInputName.text.toString(), ownerId = UserService.getUserInfo()._id)
            createChannel(newChannel)
        }
    }

    // activate/deactivate button depending on input fields
    private fun updateCreateBtn() {
        val isValid = channelLayout.error.isNullOrBlank() && !channelInputName.text.isNullOrEmpty()
        toggleButton(createChannelBtn, isValid)
    }

    private fun createChannel(newChannelModel: TextChannelModel.AllInfo) {
        val channelRepository = TextChannelRepository()

        channelRepository.addChannel(newChannelModel).observe(this, {
            closeSheet()

            if (it.isError as Boolean) {
                printToast(requireActivity(), it.message!!)
                return@observe
            }

            val channel = it.data as TextChannelModel.AllInfo
            TextChannelService.createNewChannel(channel)
            TextChannelService.refreshChannelList()
            ChatService.refreshChatBox(context as FragmentActivity)
        })
    }
}