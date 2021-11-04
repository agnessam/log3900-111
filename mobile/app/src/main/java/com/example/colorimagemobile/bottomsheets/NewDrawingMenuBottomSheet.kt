package com.example.colorimagemobile.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import com.example.colorimagemobile.R
import com.example.colorimagemobile.utils.CommonFun.Companion.toggleButton
import com.example.colorimagemobile.utils.Constants.DRAWING.Companion.MAX_HEIGHT
import com.example.colorimagemobile.utils.Constants.DRAWING.Companion.MAX_WIDTH
import com.example.colorimagemobile.utils.Constants.DRAWING.Companion.MIN_HEIGHT
import com.example.colorimagemobile.utils.Constants.DRAWING.Companion.MIN_WIDTH
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NewDrawingMenuBottomSheet: BottomSheetDialogFragment() {
    private lateinit var createDrawingBtn: Button
    private lateinit var widthLayout: TextInputLayout
    private lateinit var heightLayout: TextInputLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_drawing_menu, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: BottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createDrawingBtn = view.findViewById(R.id.createDrawingBtn)
        widthLayout = view.findViewById(R.id.newDrawingWidthInputLayout)
        heightLayout = view.findViewById(R.id.newDrawingHeightInputLayout)

        toggleButton(createDrawingBtn, false)

        // width input validation
        view.findViewById<TextInputEditText>(R.id.newDrawingWidthInputText).doOnTextChanged { text, _, _, _ ->
            widthLayout.error = getErrorMessage(getCurrentValue(text), MIN_WIDTH, MAX_WIDTH)
            updateCreateBtn()
        }

        // height input validation
        view.findViewById<TextInputEditText>(R.id.newDrawingHeightInputText).doOnTextChanged { text, _, _, _ ->
            heightLayout.error = getErrorMessage(getCurrentValue(text), MIN_HEIGHT, MAX_HEIGHT)
            updateCreateBtn()
        }
    }

    // read input field and convert to int
    private fun getCurrentValue(text: CharSequence?): Int {
        val currentText = text!!.toString()
        return if(currentText == "") 0 else currentText.toInt()
    }

    // check input field's value range
    private fun getErrorMessage(currentValue: Int, min: Int, max: Int): String? {
        return if (currentValue < min || currentValue > max) getSizeError(min, max) else null
    }

    // error to show if input is invalid
    private fun getSizeError(min: Int, max: Int): String {
        return "Value must be between ${min}px and ${max}px"
    }

    // activate/deactivate button depending on input fields
    private fun updateCreateBtn() {
        val isValid = widthLayout.error == null && heightLayout.error == null
        toggleButton(createDrawingBtn, isValid)
    }
}