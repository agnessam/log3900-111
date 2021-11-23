package com.example.colorimagemobile.ui.userProfile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.users.UserService


class UserProfileHistoryFragment : Fragment() {
    private lateinit var lastLogin : TextView
    private lateinit var lastLogout : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate layout
        val inf: View = inflater.inflate(R.layout.fragment_user_profile_history, container, false)

        // find the texView
        lastLogin= inf.findViewById<View>(R.id.lastLogin) as TextView
        lastLogout = inf.findViewById<View>(R.id.lastLogout) as TextView



        return inf
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = UserService.getUserInfo()

        // sets the derived data in the textView
        lastLogin.text = user.lastLoginDate
        lastLogout.text =user.lastLogoutDate
    }



}

