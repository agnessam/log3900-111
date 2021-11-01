package com.example.colorimagemobile.ui.userProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
<<<<<<< HEAD
import androidx.lifecycle.LiveData
=======
>>>>>>> dbd93941bd50057e33c6e1da869451e0362138ec
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.services.HandleHTTP
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.ui.home.HomeActivity
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.Constants

class EditProfileFragment : Fragment() {

<<<<<<< HEAD
    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var userRepository: UserRepository
    private lateinit var edtDescription: String
    private lateinit var infDescription: TextView
    private lateinit var edtUsername: String
    private lateinit var handleHTTP: HandleHTTP
    private lateinit var infName: TextView
    private var infview : View ? = null
    private lateinit var token : String
    private lateinit var user : UserModel.AllInfo

=======
//    private lateinit var editViewBinding: View
//    private lateinit var fName: TextView
//    private lateinit var lName: TextView
>>>>>>> dbd93941bd50057e33c6e1da869451e0362138ec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = UserService.getUserInfo()
        userRepository = UserRepository()
        handleHTTP = HandleHTTP()
        sharedPreferencesService = context?.let { SharedPreferencesService(it) }!!
        token = sharedPreferencesService.getItem(Constants.STORAGE_KEY.TOKEN)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
<<<<<<< HEAD
        // inflate layout
        val inf = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        // listeners
        inf.findViewById<View>(R.id.updatebutton).setOnClickListener { update() }
        inf.findViewById<View>(R.id.editprofileview).setOnTouchListener { v, event -> CommonFun.closeKeyboard_(this.requireActivity()) }

       // keyboard
        CommonFun.onEnterKeyPressed_(inf.findViewById<View>(R.id.edtusername) as TextView) { update() }
        CommonFun.onEnterKeyPressed_(inf.findViewById<View>(R.id.edtdescription) as TextView) { update() }
        infview = inf

        return inf
    }

    private fun areFieldEmpty(): Boolean {
        var required: Boolean = false
        var view: View? = null

        infName = (infview!!.findViewById<View>(R.id.edtusername) as TextView)
        infDescription = (infview!!.findViewById<View>(R.id.edtdescription) as TextView)
        edtUsername = infName.text.toString()
        edtDescription = infDescription.text.toString()

        if (edtUsername.length == 0) {
            infName.error = "Field is required"
            required = true
            view = infName

        } else if (edtDescription.length == 0) {
            infDescription.error = "Field is required"
            required = true
            view = infDescription
        }

        return if (required) {
            view?.requestFocus()
            true
        } else false
    }

    private fun update(){
        if (areFieldEmpty()) return

        // form body to make HTTP request
        val newUserData = UserModel.UpdateUser(edtUsername, edtDescription, user.password)
        val updateObserver = updateUserInfo(newUserData)

        updateObserver.observe(viewLifecycleOwner, { context?.let { it1 -> handleHTTP.Response(it1,it) } })
        CommonFun.redirectTo_(this.requireActivity(), HomeActivity::class.java)

    }

    private fun updateUserInfo(newUserData: UserModel.UpdateUser): LiveData<DataWrapper<HTTPResponseModel.UpdateUser>> {
        return userRepository.updateUserData(token, user._id,newUserData)
    }
=======
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

>>>>>>> dbd93941bd50057e33c6e1da869451e0362138ec


}