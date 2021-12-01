package com.example.colorimagemobile.ui.home.fragments.accountParameter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import com.example.colorimagemobile.R

class PrivacyAndSafety : Fragment() {
     private lateinit var switch_email : SwitchCompat
     private lateinit var switch_firstname : SwitchCompat
     private lateinit var switch_lastname : SwitchCompat


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setListeners()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_privacy_and_safety, container, false)
        switch_email = inf.findViewById(R.id.switch_email)
        switch_firstname = inf.findViewById(R.id.switch_firstname)
        switch_lastname = inf.findViewById(R.id.switch_lastname)
        return inf
    }

    private fun setListeners(){
        switch_email.setOnCheckedChangeListener{ compoundButton,isEmailAllow ->
           if(isEmailAllow){}
            else{}

        }

        switch_firstname.setOnCheckedChangeListener{ compoundButton,isFirstnameAllow ->

        }

        switch_lastname.setOnCheckedChangeListener{ compoundButton,isLastNameAllow ->

        }

    }

}