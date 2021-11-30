package com.example.colorimagemobile.ui.home.fragments.accountParameter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.ui.userProfile.EditProfileFragment
import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.w3c.dom.Text


class UserProfileFragment : Fragment() {

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
        myPrivacy.setTextColor(Color.parseColor("#4050b5"))
        val showUserProfile = ShowUserProfileFragment()
        parentFragmentManager.beginTransaction().replace(R.id.newLayout, showUserProfile)
            .commit()


        view.findViewById<TextView>(R.id.myPrivacy).setOnClickListener {

            // to be change after i create view for account visibility
            changeTxtColor(myPrivacy,modifyprofile,history,stat,history)
            val showUserProfile = ShowUserProfileFragment()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, showUserProfile)
                .commit()

        }
        view.findViewById<TextView>(R.id.modifyprofile).setOnClickListener {

            changeTxtColor(modifyprofile,myPrivacy,history,stat,history)
            val editProfileFragment = EditProfileFragment()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, editProfileFragment)
                .commit()

        }
        view.findViewById<TextView>(R.id.history).setOnClickListener {

            changeTxtColor(history,modifyprofile,myPrivacy,stat,history)
            val history = UserProfileHistoryFragment()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, history)
                .commit()


        }
        view.findViewById<TextView>(R.id.stat).setOnClickListener {

            changeTxtColor(stat,modifyprofile,myPrivacy,history,history)
            val stat = UserStatistic()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, stat)
                .commit()


        }

    }

    private fun changeTxtColor(selectedTxt: TextView, menuTxt1: TextView,menuTxt2: TextView,menuTxt3: TextView,menuTxt4: TextView) {
        selectedTxt.setTextColor(Color.parseColor("#4050b5"))
        menuTxt1.setTextColor(Color.parseColor("#888888"))
        menuTxt2.setTextColor(Color.parseColor("#888888"))
        menuTxt3.setTextColor(Color.parseColor("#888888"))
        menuTxt4.setTextColor(Color.parseColor("#888888"))
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