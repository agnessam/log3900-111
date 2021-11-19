package com.example.colorimagemobile.bottomsheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.CreateTeamModel
import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.repositories.TeamRepository
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.services.teams.TeamAdapterService
import com.example.colorimagemobile.services.teams.TeamService
import com.example.colorimagemobile.utils.CommonFun.Companion.toggleButton
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NewTeamBottomSheet: BottomSheetDialogFragment() {
    private lateinit var createTeamBtn: Button
    private lateinit var nameLayout: TextInputLayout
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var nameInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var dialog: BottomSheetDialog

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

        createTeamBtn = view.findViewById(R.id.createTeamBtn)

        toggleButton(createTeamBtn, false)
        setListeners()
    }

    private fun setListeners() {
        nameInput.doOnTextChanged { text, _, _, _ ->
            nameLayout.error = if (text.toString().isEmpty()) "Team name can't be empty" else null
            updateCreateBtn()
        }

        descriptionInput.doOnTextChanged { text, _, _, _ ->
            descriptionLayout.error = if (text.toString().isEmpty()) "Team description can't be empty" else null
            updateCreateBtn()
        }

        createTeamBtn.setOnClickListener { createTeam() }
    }

    // activate/deactivate button depending on input fields
    private fun updateCreateBtn() {
        val areLayoutsValid = nameLayout.error == null && descriptionLayout.error == null
        val areInputsValid = nameInput.text!!.isNotEmpty() && descriptionInput.text!!.isNotEmpty()
        toggleButton(createTeamBtn, areLayoutsValid && areInputsValid)
    }

    private fun createTeam() {
        val newTeam = CreateTeamModel(name = nameInput.text.toString(), description = descriptionInput.text.toString(), ownerId = UserService.getUserInfo()._id)

        TeamRepository().createTeam(newTeam).observe(context as LifecycleOwner, {
            closeSheet()

            if (it.isError as Boolean) {
                return@observe
            }

            val team = it.data as TeamModel
            TeamService.addTeam(team)
            TeamAdapterService.getTeamMenuAdapter().notifyDataSetChanged()
        })
    }
}