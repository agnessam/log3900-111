package com.example.colorimagemobile.ui.userProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.utils.Constants


class UserProfileFragment : Fragment() {
//    private lateinit var userRepository: UserRepository
//    private lateinit var sharedPreferencesService: SharedPreferencesService
//    private lateinit var token : String
//    private lateinit var user : UserModel.AllInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        userRepository = UserRepository()
//        sharedPreferencesService = context?.let { SharedPreferencesService(it) }!!
//        token = sharedPreferencesService.getItem(Constants.STORAGE_KEY.TOKEN)
//        user = UserService.getUserInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val showUserProfile = ShowUserProfileFragment()
        parentFragmentManager.beginTransaction().replace(R.id.newLayout, showUserProfile)
            .commit()


        view.findViewById<TextView>(R.id.parametredecompt).setOnClickListener {
            val showUserProfile = ShowUserProfileFragment()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, showUserProfile)
                .commit()

        }
        view.findViewById<TextView>(R.id.modifyprofile).setOnClickListener {
            val editProfileFragment = EditProfileFragment()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, editProfileFragment)
                .commit()

        }
        view.findViewById<TextView>(R.id.password).setOnClickListener {
            val passwordFragment = PasswordFragment()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, passwordFragment)
                .commit()

        }
        view.findViewById<TextView>(R.id.historique).setOnClickListener {
            val history = UserProfileHistoryFragment()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, history)
                .commit()


        }

    }

//     fun updateUserInfo(newUserData: UserModel.UpdateUser): LiveData<DataWrapper<HTTPResponseModel.UpdateUser>> {
//        return userRepository.updateUserData(token, user._id,newUserData)
//    }

}