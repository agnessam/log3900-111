package com.example.colorimagemobile.bottomsheets

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.widget.doOnTextChanged
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.TeamIdModel
import com.example.colorimagemobile.models.UpdateTeam
import com.example.colorimagemobile.utils.CommonFun.Companion.hideKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.onEnterKeyPressed
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import kotlinx.android.synthetic.main.bottomsheet_edit_team.*

class EditTeamBottomSheet(
    private val activity: Activity,
    private val team: TeamIdModel,
    val updateTeam: (teamUpdateData: UpdateTeam) -> Unit
): BottomSheetDialogFragment() {
    private lateinit var updateBtn: Button
    private lateinit var descriptionLayout: TextInputLayout
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var passwordInput: TextInputEditText
    private lateinit var dialog: BottomSheetDialog
    private lateinit var privacyCheckBox: CheckBox

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_edit_team, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun closeSheet() { dialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.editTeamFormTitle).text = "Editing the parameters of ${team.name}"

        descriptionLayout = view.findViewById(R.id.editTeamDescLayout)
        descriptionInput = view.findViewById(R.id.editTeamDescInputText)
        descriptionInput.setText(team.description)

        passwordLayout = view.findViewById(R.id.editTeamPrivatePasswordInputLayout)
        passwordInput = view.findViewById(R.id.editTeamPrivatePasswordInputText)
        updateBtn = view.findViewById(R.id.updateTeamBtn)

        privacyCheckBox = view.findViewById(R.id.editTeamPrivateCheckbox)
        privacyCheckBox.isChecked = team.isPrivate
        togglePasswordInput(team.isPrivate)

        setListeners()
    }

    private fun togglePasswordInput(shouldEnable: Boolean) {
        passwordLayout.alpha = if (shouldEnable) 1f else .4f
        passwordLayout.isClickable = shouldEnable
        passwordLayout.isEnabled = shouldEnable
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        passwordInput.doOnTextChanged { text, _, _, _ ->
            passwordLayout.error = if (text.toString().isEmpty()) "Password can't be empty" else null
        }

        privacyCheckBox.setOnCheckedChangeListener { _, isChecked -> togglePasswordInput(isChecked) }

        editTeamForm.setOnTouchListener{_, _ -> hideKeyboard(activity, editTeamForm) }
        updateBtn.setOnClickListener { onUpdate() }
        onEnterKeyPressed(passwordInput) { onUpdate() }
        onEnterKeyPressed(descriptionInput) { onUpdate() }
    }

    private fun shakeForm() {
        val shake = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.shake)
        editTeamForm.startAnimation(shake);
    }

    private fun onUpdate() {
        val enteredPassword = passwordInput.text.toString()
        val enteredDescription = descriptionInput.text.toString()
        val newPrivacy = privacyCheckBox.isChecked

        if (enteredDescription.isNullOrEmpty()) {
            shakeForm()
            return
        }

        if (newPrivacy && enteredPassword.isNullOrEmpty()) {
            printToast(activity, "Password can't be empty")
            shakeForm()
            return
        }

        val updateTeamData = UpdateTeam(description=enteredDescription, isPrivate=newPrivacy)
        if (newPrivacy) updateTeamData.password = enteredPassword

        updateTeam(updateTeamData)
        closeSheet()
    }
}