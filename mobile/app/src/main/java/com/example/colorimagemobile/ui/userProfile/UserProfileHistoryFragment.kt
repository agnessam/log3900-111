package com.example.colorimagemobile.ui.userProfile


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.UserService


class UserProfileHistoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lastlogindate"," dans historique date = "+arguments)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate layout
        val inf: View = inflater.inflate(R.layout.fragment_user_profile_history, container, false)

        // find the texView
        val lastLogin= inf.findViewById<View>(R.id.lastLogin) as TextView
        val lastLogout = inf.findViewById<View>(R.id.lastLogout) as TextView
        val log : ArrayList<String> = UserService.getLogHistory()

       // sets the derived data in the textView
        lastLogin.text = log.get(0)
        lastLogout.text =log.get(1)

        Log.d("lastlogindate"," dans historique date = "+log.get(0))
        Log.d("lastlogoutdate"," dans historique date = "+log.get(1))
        return inf
    }

}

