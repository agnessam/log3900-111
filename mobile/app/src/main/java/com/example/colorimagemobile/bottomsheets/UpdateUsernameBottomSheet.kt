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
import androidx.fragment.app.FragmentActivity
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.classes.UpdateUserProfile
import com.example.colorimagemobile.httpresponsehandler.GlobalHandler
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.bottomsheet_create_channel.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_update_username_bottom_sheet.*
import com.example.colorimagemobile.ui.home.fragments.accountParameter.accountParameterFragment
import com.example.colorimagemobile.utils.Constants


class UpdateUsernameBottomSheet : BottomSheetDialogFragment() {
    private lateinit var changeUsernameBtn: Button
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var usernameInputName: TextInputEditText
    private lateinit var newUserData : UserModel.UpdateUser
    private lateinit var dialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newUserData = UserModel.UpdateUser(null,null,null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_update_username_bottom_sheet,
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
        changeUsernameBtn = view.findViewById(R.id.changeUsernameBtn)
        usernameLayout = view.findViewById(R.id.newUsernameInputLayout)
        usernameInputName = view.findViewById(R.id.newUsernameInputText)
        setListeners(view)
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners(view: View) {
        usernameInputName.doOnTextChanged { text, _, _, _ ->
            usernameLayout.error = ""
            if (text.isNullOrEmpty()) {
                usernameLayout.error = "The name can not be empty"
                return@doOnTextChanged
            } else if (text.length<Constants.MIN_LENGTH || text.length > Constants.MAX_LENGTH){
                usernameLayout.error = "The name length should be min 4 and max 14"
                return@doOnTextChanged
            }
        }
        updateUsernameForm.setOnTouchListener{v, event ->
            CommonFun.hideKeyboard(
                requireContext(),
                updateUsernameForm
            )
        }

        changeUsernameBtn.setOnClickListener {
            if(!usernameLayout.error.isNullOrBlank() || usernameInputName.text.isNullOrEmpty()){
                val shake = AnimationUtils.loadAnimation(requireActivity().getApplicationContext(), R.anim.shake)
                newUsernameInputText.startAnimation(shake);
                return@setOnClickListener
            }
            newUserData.username = usernameInputName.text.toString()
            UpdateUserProfile().updateUserInfo(newUserData).observe(viewLifecycleOwner, { context?.let { it1 -> GlobalHandler().response(it1,it) } })
            UserService.updateMe(newUserData)
            requireActivity().invalidateOptionsMenu()
            MyFragmentManager(context as FragmentActivity).open(R.id.userProfileFragment,
                accountParameterFragment()
            )
            closeSheet()
        }
    }


}