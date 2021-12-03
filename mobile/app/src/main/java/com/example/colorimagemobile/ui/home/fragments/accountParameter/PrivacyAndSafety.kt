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
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import com.example.colorimagemobile.utils.Constants

class PrivacyAndSafety : Fragment(R.layout.fragment_privacy_and_safety) {
     private lateinit var sharedPreferencesService: SharedPreferencesService
     private lateinit var myView : View
     private lateinit var switch_email : SwitchCompat
     private lateinit var switch_firstname : SwitchCompat
     private lateinit var switch_lastname : SwitchCompat
     private var newSetting: Privacy.Setting = Privacy.Setting(false,false,false)

    private fun setListeners(){
        switch_email.setOnCheckedChangeListener{ compoundButton,isEmailAllow ->
            newSetting.searchableByEmail = isEmailAllow
            newSetting.searchableByFirstName = UserService.getUserInfo().privacySetting.searchableByFirstName
            newSetting.searchableByLastName =UserService.getUserInfo().privacySetting.searchableByLastName
            updatePrivacySetting()
            sharedPreferencesService.setItem(Constants.STORAGE_KEY.PRIVACY_EMAIL, isEmailAllow)
            printMsg("value after setting email "+isEmailAllow.toString())
        }

        switch_firstname.setOnCheckedChangeListener{ compoundButton,isFirstnameAllow ->
            newSetting.searchableByFirstName = isFirstnameAllow
            newSetting.searchableByEmail = UserService.getUserInfo().privacySetting.searchableByEmail
            newSetting.searchableByLastName =UserService.getUserInfo().privacySetting.searchableByLastName
            updatePrivacySetting()
            sharedPreferencesService.setItem(Constants.STORAGE_KEY.PRIVACY_FIRSTNAME, isFirstnameAllow)
        }

        switch_lastname.setOnCheckedChangeListener{ compoundButton,isLastNameAllow ->
            newSetting.searchableByLastName = isLastNameAllow
            newSetting.searchableByFirstName = UserService.getUserInfo().privacySetting.searchableByFirstName
            newSetting.searchableByEmail =UserService.getUserInfo().privacySetting.searchableByEmail
            updatePrivacySetting()
            sharedPreferencesService.setItem(Constants.STORAGE_KEY.PRIVACY_LASTNAME, isLastNameAllow)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        sharedPreferencesService = SharedPreferencesService(requireContext())
        switch_email = myView.findViewById(R.id.switch_email)
        switch_firstname = myView.findViewById(R.id.switch_firstname)
        switch_lastname = myView.findViewById(R.id.switch_lastname)
        val emailPrivacy = sharedPreferencesService.getBooleanItem(Constants.STORAGE_KEY.PRIVACY_EMAIL)
        switch_email.setChecked(emailPrivacy)
        val firstnamePrivacy = sharedPreferencesService.getBooleanItem(Constants.STORAGE_KEY.PRIVACY_FIRSTNAME)
        switch_firstname.setChecked(firstnamePrivacy)
        val lastnamePrivacy = sharedPreferencesService.getBooleanItem(Constants.STORAGE_KEY.PRIVACY_LASTNAME)
        switch_lastname.setChecked(lastnamePrivacy)
        setListeners()
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
        printToast(requireContext(),"${newSetting}")
    }

}