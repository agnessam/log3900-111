package com.example.colorimagemobile.ui.userProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.colorimagemobile.R
import com.example.colorimagemobile.bottomsheets.DefaultAvatarListBottomSheet
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.httpresponsehandler.GlobalHandler
import com.example.colorimagemobile.models.AvatarModel
import com.example.colorimagemobile.repositories.AvatarRepository
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.services.avatar.AvatarService
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.imageView
import com.example.colorimagemobile.utils.Constants
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_show_user_profile.*

class EditProfileFragment : Fragment() {

    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var userRepository: UserRepository
    private lateinit var avatarRepository : AvatarRepository
    private lateinit var edtDescription: String
    private lateinit var infDescription: TextView
    private lateinit var edtUsername: String
    private lateinit var globalHandler: GlobalHandler
    private lateinit var infName: TextView
    private lateinit var token : String
    private lateinit var user : UserModel.AllInfo
    private lateinit var currentAvatar : AvatarModel.AllInfo
    private lateinit var newUserData : UserModel.UpdateUser
    private var infview : View ? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = UserService.getUserInfo()
        userRepository = UserRepository()
        avatarRepository = AvatarRepository()
        globalHandler = GlobalHandler()
        sharedPreferencesService = context?.let { SharedPreferencesService(it) }!!
        token = sharedPreferencesService.getItem(Constants.STORAGE_KEY.TOKEN)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflate layout
        val inf = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        // listeners
        inf.findViewById<View>(R.id.updateprofile).setOnClickListener { update() }
        inf.findViewById<View>(R.id.upload_avatar_from_camera).setOnClickListener { update() }
        inf.findViewById<View>(R.id.editprofileview).setOnTouchListener { v, event -> CommonFun.closeKeyboard_(this.requireActivity()) }
        inf.findViewById<View>(R.id.choosedefaultavatar).setOnClickListener {
            val defaultAvatarList = DefaultAvatarListBottomSheet()
            defaultAvatarList.show(parentFragmentManager, "DefaultAvatarListBottomSheetDialog")
        }

       // keyboard
        CommonFun.onEnterKeyPressed_(inf.findViewById<View>(R.id.edtusername) as TextView) { update() }
        CommonFun.onEnterKeyPressed_(inf.findViewById<View>(R.id.edtdescription) as TextView) { update() }
        imageView = (inf.findViewById<View>(R.id.current_avatar) as ImageView)
        CommonFun.loadUrl(user.avatar.imageUrl, imageView )
        infview = inf
        return inf
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllAvatar()
    }

    // Get all default avatar from database
    private fun getAllAvatar(){
        AvatarRepository().getAllAvatar(UserService.getToken()).observe(context as LifecycleOwner,{
            if (it.isError as Boolean) {
                return@observe
            }
            val avatars = it.data as ArrayList<AvatarModel.AllInfo>
            AvatarService.setAvatars(avatars)
        })
    }

    // verify field are empty
    private fun areFieldEmpty(): Boolean {
        var required: Boolean = false
        var view: View? = null

        infName = (infview!!.findViewById<View>(R.id.edtusername) as TextView)
        infDescription = (infview!!.findViewById<View>(R.id.edtdescription) as TextView)
        edtUsername = infName.text.toString()
        edtDescription = infDescription.text.toString()

        if (edtUsername.length == 0) {
            infName.error = Constants.FIELD_IS_REQUIRED
            required = true
            view = infName

        } else if (edtDescription.length == 0) {
            infDescription.error = Constants.FIELD_IS_REQUIRED
            required = true
            view = infDescription
        }

        return if (required) {
            view?.requestFocus()
            true
        } else false
    }

    // update user info
    private fun update(){
        if (areFieldEmpty() && AvatarService.getCurrentAvatar().imageUrl== Constants.EMPTY_STRING ) return

        if(AvatarService.getCurrentAvatar().imageUrl!= Constants.EMPTY_STRING && !areFieldEmpty()){
            currentAvatar = AvatarService.getCurrentAvatar()
            newUserData =UserModel.UpdateUser(edtUsername, edtDescription, user.password, currentAvatar)

        }
        else if (AvatarService.getCurrentAvatar().imageUrl!= Constants.EMPTY_STRING && areFieldEmpty()){
            currentAvatar = AvatarService.getCurrentAvatar()
            user = UserService.getUserInfo()
            newUserData =UserModel.UpdateUser(user.username,user.description,user.password,currentAvatar)

        }
        else{
            currentAvatar = UserService.getUserInfo().avatar
            newUserData = UserModel.UpdateUser(edtUsername, edtDescription, user.password, currentAvatar)
        }
        // form body to make HTTP request
        UserService.setNewProfileData(newUserData)
        val updateObserver = updateUserInfo()
        updateObserver.observe(viewLifecycleOwner, { context?.let { it1 ->globalHandler.response(it1,it) } })
    }

    private fun updateUserInfo(): LiveData<DataWrapper<HTTPResponseModel.UserResponse>> {
        return userRepository.updateUserData(token, user._id)
    }


}