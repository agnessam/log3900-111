package com.example.colorimagemobile.classes

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FormValidator(layouts: ArrayList<TextInputLayout>, inputs: ArrayList<TextInputEditText>) {
    private var layouts: ArrayList<TextInputLayout> = layouts
    private var inputs: ArrayList<TextInputEditText> = inputs

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

}