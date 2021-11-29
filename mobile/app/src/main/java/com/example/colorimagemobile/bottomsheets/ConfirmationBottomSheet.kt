package com.example.colorimagemobile.bottomsheets

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.colorimagemobile.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class ConfirmationBottomSheet(
    val onConfirm: () -> Unit,
    private val title: String,
    private val description: String,
    private val buttonName: String,
    private val backgroundTint: String
): BottomSheetDialogFragment() {
    private lateinit var cancelBtn: Button
    private lateinit var confirmBtn: MaterialButton
    private lateinit var dialog: BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_delete_confirmation, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun closeSheet() { dialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.confirmationTitle).text = title
        view.findViewById<TextView>(R.id.confirmationDescription).text = description
        cancelBtn = view.findViewById(R.id.confirmationCancelBtn)
        confirmBtn = view.findViewById(R.id.confirmationConfirmBtn)

        confirmBtn.text = buttonName
        confirmBtn.background.setTint(Color.parseColor(backgroundTint))

        setListeners()
    }

    private fun setListeners() {
        cancelBtn.setOnClickListener { closeSheet() }
        confirmBtn.setOnClickListener {
            onConfirm()
            closeSheet()
        }
    }
}