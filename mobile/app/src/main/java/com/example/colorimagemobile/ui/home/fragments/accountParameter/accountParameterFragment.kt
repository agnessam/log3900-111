package com.example.colorimagemobile.ui.home.fragments.accountParameter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.ui.userProfile.EditProfileFragment
import kotlinx.android.synthetic.main.fragment_user_profile.*


class accountParameterFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUserStatistics()
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
        changeTxtColor(myProfile,privacy,history,stat)
        val editProfileFragment = EditProfileFragment()
        parentFragmentManager.beginTransaction().replace(R.id.newLayout, editProfileFragment)
            .commit()


        view.findViewById<TextView>(R.id.myProfile).setOnClickListener {

            changeTxtColor(myProfile,privacy,history,stat)
            val editProfileFragment = EditProfileFragment()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, editProfileFragment)
                .commit()

        }

        view.findViewById<TextView>(R.id.history).setOnClickListener {

            changeTxtColor(history,privacy,myProfile,stat)
            val history = HistoryFragment()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, history)
                .commit()


        }
        view.findViewById<TextView>(R.id.stat).setOnClickListener {

            changeTxtColor(stat,privacy,myProfile,history)
            val stat = UserStatistic()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, stat)
                .commit()


        }

        view.findViewById<TextView>(R.id.privacy).setOnClickListener {

            changeTxtColor(privacy,stat,myProfile,history)
            val privacyAndSetting = PrivacyAndSafety()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, privacyAndSetting)
                .commit()


        }

    }

    private fun changeTxtColor(selectedTxt: TextView, menuTxt1: TextView,menuTxt2: TextView,menuTxt3: TextView) {
        selectedTxt.setTextColor(Color.parseColor("#FF4040"))
        menuTxt1.setTextColor(Color.parseColor("#888888"))
        menuTxt2.setTextColor(Color.parseColor("#888888"))
        menuTxt3.setTextColor(Color.parseColor("#888888"))
    }
    private fun getUserStatistics(){
        UserRepository().getUserStatistics().observe(this, {
            if (it.isError as Boolean) {
                return@observe
            }
            val stat = it.data as UserModel.Statistics
            UserService.setStat(stat)
        })
    }

}