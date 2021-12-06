package com.example.colorimagemobile.bottomsheets

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.CheckBox
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.CreateTeamModel
import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.repositories.TeamRepository
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.services.teams.TeamAdapterService
import com.example.colorimagemobile.services.teams.TeamService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun.Companion.hideKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.bottomsheet_create_team.*
import kotlinx.android.synthetic.main.fragment_update_username_bottom_sheet.*

class NewTeamBottomSheet: BottomSheetDialogFragment() {
    private lateinit var createTeamBtn: Button
    private lateinit var nameLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var nameInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var memberLimitInput: TextInputEditText
    private lateinit var dialog: BottomSheetDialog
    private lateinit var memberCheckbox: CheckBox
    private lateinit var privateCheckbox: CheckBox
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var passwordInput: TextInputEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_create_team, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun closeSheet() { dialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameLayout = view.findViewById(R.id.teamNameInputLayout)
        nameInput = view.findViewById(R.id.teamNameInputText)

        descriptionLayout = view.findViewById(R.id.teamDescriptionInputLayout)
        descriptionInput = view.findViewById(R.id.teamDescriptionInputText)

        memberCheckbox = view.findViewById(R.id.createMemberLimitCheckbox)
        memberLimitInput = view.findViewById(R.id.teamMemberInputText)

        privateCheckbox = view.findViewById(R.id.createTeamPrivateCheckbox)
        passwordLayout = view.findViewById(R.id.teamPrivatePasswordInputLayout)
        passwordInput = view.findViewById(R.id.teamPrivatePasswordInputText)

        createTeamBtn = view.findViewById(R.id.createTeamBtn)

        toggleEditText(passwordInput, false)
        toggleEditText(memberLimitInput, false)
        setListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        nameInput.doOnTextChanged { text, _, _, _ ->
            nameLayout.error = if (text.toString().isEmpty()) "Team name required"
                               else if (text.toString().length < Constants.MIN_LENGTH || text.toString().length> Constants.MAX_LENGTH)
                                   "Team name length should be min 4 and max 12 characters"
                               else null
        }

        descriptionInput.doOnTextChanged { text, _, _, _ ->
            descriptionLayout.error = if (text.toString().isEmpty()) "Description required" else null
        }

        newTeamForm.setOnTouchListener{_, _ -> hideKeyboard(requireContext(), newTeamForm)}
        privateCheckbox.setOnCheckedChangeListener { _, isChecked -> toggleEditText(passwordInput, isChecked) }
        memberCheckbox.setOnCheckedChangeListener { _, isChecked -> toggleEditText(memberLimitInput, isChecked) }
        createTeamBtn.setOnClickListener { createTeam() }
    }

    private fun toggleEditText(editText: TextInputEditText, value: Boolean) {
        editText.isEnabled = value
    }

    private fun createTeam() {
        val password = if (privateCheckbox.isChecked) passwordInput.text.toString() else null
        val areLayoutsValid = nameLayout.error == null && descriptionLayout.error == null
        val areInputsValid = nameInput.text!!.isNotEmpty() && descriptionInput.text!!.isNotEmpty()

        if (privateCheckbox.isChecked && password.isNullOrEmpty()) {
            passwordLayout.error = "Password can't be empty"
            val shake = AnimationUtils.loadAnimation(requireActivity().getApplicationContext(), R.anim.shake)
            newTeamForm.startAnimation(shake);
            return
        }

        if(!areLayoutsValid || !areInputsValid){
            val shake = AnimationUtils.loadAnimation(requireActivity().getApplicationContext(), R.anim.shake)
            newTeamForm.startAnimation(shake);
            return
        }

        val memberLimit = if (memberCheckbox.isChecked) memberLimitInput.text.toString().toInt() else null
        val newTeam = CreateTeamModel(
            name = nameInput.text.toString(),
            description = descriptionInput.text.toString(),
            isPrivate = privateCheckbox.isChecked,
            owner = UserService.getUserInfo()._id,
            password = password,
            memberLimit = memberLimit)

        TeamRepository().createTeam(newTeam).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                printToast(requireContext(), "Sorry, team name is possibly already in use")
                return@observe
            }

            printToast(requireContext(), "Team successfully created")
            val team = it.data as TeamModel
            TeamService.addTeam(team)
            TeamAdapterService.getTeamMenuAdapter().notifyDataSetChanged()

            val newChannel = TextChannelModel.AllInfo(
                _id = null,
                name = team.name,
                ownerId = team.owner,
                drawing = null,
                isPrivate = true,
                team = team._id
            )

            TextChannelService.createChannel(newChannel, requireContext()) {  }
            closeSheet()
        })
    }
}