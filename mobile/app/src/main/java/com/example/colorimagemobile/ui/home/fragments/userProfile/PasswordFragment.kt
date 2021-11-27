package com.example.colorimagemobile.ui.home.fragments.userProfile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.httpresponsehandler.GlobalHandler
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.Constants
import kotlinx.android.synthetic.main.fragment_password.*

class PasswordFragment : Fragment() {

    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var userRepository: UserRepository
    private lateinit var globalHandler: GlobalHandler
    private lateinit var token : String
    private lateinit var user : UserModel.AllInfo
    private var infview : View ? = null

    private lateinit var edtPassword: String
    private lateinit var edtOldPassword: String
    private lateinit var edtMatchPassword: String

    private lateinit var oldP : TextView
    private lateinit var newP : TextView
    private lateinit var matchnewP : TextView

    private  lateinit var userProfileFragment : UserProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = UserService.getUserInfo()
        userRepository = UserRepository()
        globalHandler = GlobalHandler()
        sharedPreferencesService = context?.let { SharedPreferencesService(it) }!!
        token = sharedPreferencesService.getItem(Constants.STORAGE_KEY.TOKEN)
        userProfileFragment = UserProfileFragment()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate layout
        val inf = inflater.inflate(R.layout.fragment_password, container, false)

        // listeners  to be uncomment when update implement serverside
        inf.findViewById<View>(R.id.updatepassword).setOnClickListener { update()}

        inf.findViewById<View>(R.id.editpasswordview).setOnTouchListener { v, event -> CommonFun.hideKeyboard(requireContext(), editpasswordview)}

        // keyboard   don't delete we need it when update implement
        CommonFun.onEnterKeyPressed_(inf.findViewById<View>(R.id.oldpassword) as TextView) { update() }
        CommonFun.onEnterKeyPressed_(inf.findViewById<View>(R.id.newpassword) as TextView) { update() }
        CommonFun.onEnterKeyPressed_(inf.findViewById<View>(R.id.vnewpassword) as TextView) { update() }
        infview = inf

        // Inflate the layout for this fragment
        return inf
    }

    private fun areFieldEmpty(): Boolean {
        var required: Boolean = false
        var view: View? = null

        oldP = (infview!!.findViewById<View>(R.id.oldpassword) as TextView)
        newP = (infview!!.findViewById<View>(R.id.newpassword) as TextView)
        matchnewP = (infview!!.findViewById<View>(R.id.vnewpassword) as TextView)

       edtOldPassword = oldP.text.toString()
       edtPassword = newP.text.toString()
       edtMatchPassword = matchnewP.text.toString()
            if (edtOldPassword.length == 0) {
                oldP.error = "Field is required"
                required = true
                view = oldP

            } else if (edtPassword.length == 0) {
                newP.error = "Field is required"
                required = true
                view = newP
            }else if (edtMatchPassword.length == 0) {
                matchnewP.error = "Field is required"
                required = true
                view = matchnewP
            }

            return if (required) {
                view?.requestFocus()
                true
            } else false

    }

    private fun NewPasswordMatch(): Boolean{
        edtPassword = (infview!!.findViewById<View>(R.id.newpassword) as TextView).text.toString()
        edtMatchPassword = (infview!!.findViewById<View>(R.id.vnewpassword) as TextView).text.toString()
        if(edtPassword  != edtMatchPassword){
            context?.let { CommonFun.printToast(it, "Password doesn't match") }
            return false
        }
        else {
            return true
        }
    }

    private fun update(){
        if (areFieldEmpty() || !NewPasswordMatch()){return}
        val userId = UserService.getUserInfo()._id
        val updateData = UserModel.PasswordUpdate(userId,edtPassword,edtMatchPassword)
        updatePassword(updateData).observe(viewLifecycleOwner, { context?.let { it1 ->globalHandler.response(it1,it) } })
        //clear all field
        clearTextField()
    }

    private fun clearTextField() {
        oldP.setText("")
        newP.setText("")
        matchnewP.setText("")

    }

    //call retrofit request to database to update user info
    private fun updatePassword(newPassword: UserModel.PasswordUpdate): LiveData<DataWrapper<HTTPResponseModel.UserResponse>> {
        return UserRepository().updateUserPassword(newPassword)
    }

}