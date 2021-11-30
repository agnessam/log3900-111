package com.example.colorimagemobile.bottomsheets

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_update_description_bottom_sheet.*


class UpdateDescriptionBottomSheet : BottomSheetDialogFragment() {
    private lateinit var changeDescriptionBtn: Button
    private lateinit var cancelBtn: Button
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var descriptionInputName: TextInputEditText
    private lateinit var dialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_update_description_bottom_sheet,
            container,
            false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun closeSheet() { dialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeDescriptionBtn = view.findViewById(R.id.changeDescriptionBtn)
        cancelBtn = view.findViewById(R.id.cancelDescriptionBtn)
        descriptionLayout = view.findViewById(R.id.newDescriptionInputLayout)
        descriptionInputName = view.findViewById(R.id.newDescriptionInputText)
        setListeners(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners(view: View) {
        descriptionInputName.doOnTextChanged { text, _, _, _ ->
            descriptionLayout.error = ""
            if (text.isNullOrEmpty()) {
                descriptionLayout.error = "The Description can not be empty"
                return@doOnTextChanged
            }
        }
        updateDescriptionForm.setOnTouchListener{v, event ->
            CommonFun.hideKeyboard(
                requireContext(),
                updateDescriptionForm
            )
        }

        changeDescriptionBtn.setOnClickListener {
            if(!descriptionLayout.error.isNullOrBlank() || descriptionInputName.text.isNullOrEmpty()){
                val shake = AnimationUtils.loadAnimation(requireActivity().getApplicationContext(), R.anim.shake)
                newDescriptionInputText.startAnimation(shake);
                return@setOnClickListener
            }
            UserService.setTemporaryDescription(descriptionInputName.text.toString())
            CommonFun.printToast(
                requireContext(),
                "Description change temporary to ${descriptionInputName.text.toString()}. It will be effective after you validate"
            )
            closeSheet()
        }
        cancelDescriptionBtn.setOnClickListener {
            closeSheet()
        }

    }

}