package com.example.colorimagemobile.classes

import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FormValidator(layouts: ArrayList<TextInputLayout>, inputs: ArrayList<TextInputEditText>) {
    private var layouts: ArrayList<TextInputLayout> = layouts
    private var inputs: ArrayList<TextInputEditText> = inputs

    // returns default error message underneath input: checks if text contains a space
    fun getWhitespaceText(text: CharSequence?): String {
        return if (text!!.contains(" ")) "No spaces are allowed!" else ""
    }

    // contains some kind of error in the form
    fun containsError(): Boolean {
        for (layout in this.layouts) {
            // return since we know there is an error
            if (!layout.error.isNullOrEmpty()) {
                return true
            }
        }

        return false
    }

    // check if inputs are empty
    fun isInputEmpty(requiredString: String): Boolean {
        var isInputInvalid: Boolean = false

        this.inputs.forEachIndexed { index, input ->
            var helperText: String = ""

            if (input.text.isNullOrEmpty()) {
                isInputInvalid = true
                helperText = requiredString
            }

            // show or hide required keyword below inputs
            this.layouts[index].helperText = helperText
        }

        return isInputInvalid
    }

    // check if email is valid: must pass email index of the form
    fun validateEmail(emailIndex: Int) {
        val email = this.inputs[emailIndex].text.toString()
        val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        this.layouts[emailIndex].error = if (isEmailValid) "" else "Invalid Email Address"
    }

    // check if username length is valid
    fun validateUsernameLength(usernameIndex: Int) {
        val username = this.inputs[usernameIndex].text.toString()
        val isUsernameInvalid = username.length < Constants.MIN_LENGTH || username.length> Constants.MAX_LENGTH;
        this.layouts[usernameIndex].error = if (!isUsernameInvalid) "" else "The username length should be min 4 and max 12 characters"
    }

    // check if password is valid
    fun validatePasswordLength(passwordIndex: Int) {
        val password= this.inputs[passwordIndex].text.toString()
        val isPasswordInvalid = password.length < Constants.PASSWORD_MIN_LENGTH;
        this.layouts[passwordIndex].error = if (!isPasswordInvalid) "" else "The password length should be at least 8 characters"
    }

    fun isInputLengthShort(lengthShortString: String): Boolean {
        var isInputLengthShort: Boolean = false

        this.inputs.forEachIndexed { index, input ->
            var helperText: String = ""

            if (input.text!!.length <8) {
                isInputLengthShort = true
                helperText = lengthShortString
            }

            this.layouts[index].helperText = helperText
        }

        return isInputLengthShort
    }
}