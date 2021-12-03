package com.example.colorimagemobile.ui.home.fragments.accountParameter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.LiveData
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.Privacy
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast

class PrivacyAndSafety : Fragment(R.layout.fragment_privacy_and_safety) {
     private lateinit var sharedPreferencesService: SharedPreferencesService
     private lateinit var myView : View
     private lateinit var switch_email : SwitchCompat
     private lateinit var switch_firstname : SwitchCompat
     private lateinit var switch_lastname : SwitchCompat
     private var newSetting : UserModel.updatePrivacy = UserModel.updatePrivacy(UserModel.privacySetting(false,false,false))

    private fun setListeners(){
        switch_email.setOnCheckedChangeListener{ compoundButton,isEmailAllow ->
            newSetting.privacySetting.searchableByEmail = UserService.getUserInfo().privacySetting.searchableByEmail
            newSetting.privacySetting.searchableByFirstName = UserService.getUserInfo().privacySetting.searchableByFirstName
            newSetting.privacySetting.searchableByLastName =UserService.getUserInfo().privacySetting.searchableByLastName
            updatePrivacySetting()
        }

        switch_firstname.setOnCheckedChangeListener{ compoundButton,isFirstnameAllow ->
            newSetting.privacySetting.searchableByFirstName = isFirstnameAllow
            newSetting.privacySetting.searchableByEmail = UserService.getUserInfo().privacySetting.searchableByEmail
            newSetting.privacySetting.searchableByLastName =UserService.getUserInfo().privacySetting.searchableByLastName
            updatePrivacySetting()
        }

        switch_lastname.setOnCheckedChangeListener{ compoundButton,isLastNameAllow ->
            newSetting.privacySetting.searchableByLastName = isLastNameAllow
            newSetting.privacySetting.searchableByFirstName = UserService.getUserInfo().privacySetting.searchableByFirstName
            newSetting.privacySetting.searchableByEmail =UserService.getUserInfo().privacySetting.searchableByEmail
            updatePrivacySetting()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        sharedPreferencesService = SharedPreferencesService(requireContext())
        switch_email = myView.findViewById(R.id.switch_email)
        switch_firstname = myView.findViewById(R.id.switch_firstname)
        switch_lastname = myView.findViewById(R.id.switch_lastname)

        val privacy = UserService.getUserInfo().privacySetting
        printMsg("settings "+privacy)
        switch_email.setChecked(privacy.searchableByEmail)
        switch_firstname.setChecked(privacy.searchableByFirstName)
        switch_lastname.setChecked(privacy.searchableByLastName)
        setListeners()
    }

    private fun updatePrivacySetting(){
        update(newSetting).observe(viewLifecycleOwner, { context?.let { it1 -> responseHandler(it1,it) } })
    }

    private fun update(newSetting: UserModel.updatePrivacy): LiveData<DataWrapper<HTTPResponseModel.UserResponse>> {
        return UserRepository().updateUserPrivacy(newSetting)
    }

    private fun responseHandler(context : Context, HTTPResponse: DataWrapper<HTTPResponseModel.UserResponse>) {
        // some error occurred during HTTP request
        if (HTTPResponse.isError as Boolean) {
            context.let { CommonFun.printToast(it, HTTPResponse.message as String) }
            return
        }
        // request successfully
        UserService.updatePrivacySetting(newSetting.privacySetting)
        printToast(requireContext(),"${newSetting}")
    }

}