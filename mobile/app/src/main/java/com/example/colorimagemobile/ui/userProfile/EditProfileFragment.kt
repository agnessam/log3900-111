package com.example.colorimagemobile.ui.userProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.colorimagemobile.R


class EditProfileFragment : Fragment() {

//    private lateinit var editViewBinding: View
//    private lateinit var fName: TextView
//    private lateinit var lName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//       editViewBinding = inflater.inflate(R.layout.fragment_edit_profile, container, false)

            return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

//    private fun areFieldEmpty(): Boolean {
//
////        val lastLogin= editViewBinding.findViewById<View>(R.id.lastLogin) as TextView
//
//        fName = editViewBinding.findViewById<View>(R.id.edtusername)
//        lName = editViewBinding.edtdescription.text.toString()
//        var required: Boolean = false
//        var view: View? = null
//
//        if (fName.isEmpty()) {
//            editViewBinding.edtFName.error = "Field is required"
//            required = true
//            view = editViewBinding.edtFName
//
//        } else if (lName.isEmpty()) {
//            editViewBinding.edtLName.error = "Field is required"
//            required = true
//            view = editViewBinding.edtLName
//
//        }
//
//        return if (required) {
//            view?.requestFocus()
//            false
//        } else true
//    }



}