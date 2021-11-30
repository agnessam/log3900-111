package com.example.colorimagemobile.bottomsheets

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LiveData
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.FormValidator
import com.example.colorimagemobile.httpresponsehandler.GlobalHandler
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_update_password_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_update_password_bottom_sheet.confirmPasswordInputText

class UpdatePasswordBottomSheet : BottomSheetDialogFragment(){

    private lateinit var updatePasswordLayouts: ArrayList<TextInputLayout>
    private lateinit var updatePasswordInputs: ArrayList<TextInputEditText>
    private lateinit var formValidator: FormValidator
    private var validForm: Boolean = true
    private lateinit var dialog: BottomSheetDialog
    private lateinit var globalHandler: GlobalHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_update_password_bottom_sheet,
            container,
            false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun closeSheet() { dialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        globalHandler = GlobalHandler()
        updatePasswordLayouts = arrayListOf<TextInputLayout>(
            currentPasswordInputLayout,
            newPasswordInputLayout,
            confirmNewPasswordInputLayout)

        updatePasswordInputs = arrayListOf<TextInputEditText>(
            currentPasswordInputText,
            newPasswordInputText,
            confirmPasswordInputText)

        formValidator = FormValidator(updatePasswordLayouts, updatePasswordInputs)

        // add text listener to each input
        updatePasswordInputs.forEachIndexed { index, input ->
            input.doOnTextChanged { text, start, before, count ->
                handleInputError(text, updatePasswordLayouts[index])
            }
        }

        updatePasswordForm.setOnTouchListener{v, event ->
            CommonFun.hideKeyboard(
                requireContext(),
                updatePasswordForm
            )}

        confirmUpdatePasswordBtn.setOnClickListener { update() }
        cancelUpadtePasswordBtn.setOnClickListener { closeSheet() }

    }

    private fun handleInputError(text: CharSequence?, inputLayout: TextInputLayout) {
        inputLayout.error = formValidator.getWhitespaceText(text)
        val containsError = formValidator.containsError()
        val invalidInputLength = formValidator.isInputEmpty(resources.getString(R.string.required))
        validForm = !containsError && !invalidInputLength


    }

    private fun NewPasswordMatch(): Boolean{
        if(newPasswordInputText.text.toString() != confirmPasswordInputText.text.toString()){
            context?.let { CommonFun.printToast(it, "Password doesn't match") }
            return false
        }
        else {
            return true
        }
    }

    private fun update(){
        updatePasswordInputs.forEachIndexed {
                index,input -> handleInputError( input.text, updatePasswordLayouts[index])
        }

        if (!validForm || !NewPasswordMatch() ) {
            val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
            updatePasswordForm.startAnimation(shake);
            return
        }

        val userId = UserService.getUserInfo()._id
        val updateData = UserModel.PasswordUpdate(userId,currentPasswordInputText.text.toString(),confirmPasswordInputText.text.toString())
        updatePassword(updateData).observe(viewLifecycleOwner, { context?.let { it1 ->globalHandler.response(it1,it) } })
        closeSheet()
    }

    private fun updatePassword(newPassword: UserModel.PasswordUpdate): LiveData<DataWrapper<HTTPResponseModel.UserResponse>> {
        return UserRepository().updateUserPassword(newPassword)
    }

}