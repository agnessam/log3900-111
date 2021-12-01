package com.example.colorimagemobile.ui.home.fragments.accountParameter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.LiveData
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.Privacy
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun

class PrivacyAndSafety : Fragment() {
     private lateinit var switch_email : SwitchCompat
     private lateinit var switch_firstname : SwitchCompat
     private lateinit var switch_lastname : SwitchCompat
     private var newSetting: Privacy.Setting = Privacy.Setting(false,false,false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_privacy_and_safety, container, false)
        switch_email = inf.findViewById(R.id.switch_email)
        switch_firstname = inf.findViewById(R.id.switch_firstname)
        switch_lastname = inf.findViewById(R.id.switch_lastname)
        setListeners()
        return inf
    }

    private fun setListeners(){
        switch_email.setOnCheckedChangeListener{ compoundButton,isEmailAllow ->
            newSetting.searchableByEmail = isEmailAllow
            newSetting.searchableByFirstName = UserService.getUserInfo().privacySetting.searchableByFirstName
            newSetting.searchableByLastName =UserService.getUserInfo().privacySetting.searchableByLastName
            updatePrivacySetting()
        }

        switch_firstname.setOnCheckedChangeListener{ compoundButton,isFirstnameAllow ->
            newSetting.searchableByFirstName = isFirstnameAllow
            newSetting.searchableByEmail = UserService.getUserInfo().privacySetting.searchableByEmail
            newSetting.searchableByLastName =UserService.getUserInfo().privacySetting.searchableByLastName
            updatePrivacySetting()
        }

        switch_lastname.setOnCheckedChangeListener{ compoundButton,isLastNameAllow ->
            newSetting.searchableByLastName = isLastNameAllow
            newSetting.searchableByFirstName = UserService.getUserInfo().privacySetting.searchableByFirstName
            newSetting.searchableByEmail =UserService.getUserInfo().privacySetting.searchableByEmail
            updatePrivacySetting()
        }

    }

    private fun updatePrivacySetting(){
        update(newSetting).observe(viewLifecycleOwner, { context?.let { it1 -> responseHandler(it1,it) } })
    }

    private fun update(newSetting: Privacy.Setting): LiveData<DataWrapper<HTTPResponseModel.UserResponse>> {
        return UserRepository().updateUserPrivacy(newSetting)
    }

    private fun responseHandler(context : Context, HTTPResponse: DataWrapper<HTTPResponseModel.UserResponse>) {
        // some error occurred during HTTP request
        if (HTTPResponse.isError as Boolean) {
            context.let { CommonFun.printToast(it, HTTPResponse.message as String) }
            return
        }
        // request successfully
        UserService.updatePrivacySetting(newSetting)
    }

}