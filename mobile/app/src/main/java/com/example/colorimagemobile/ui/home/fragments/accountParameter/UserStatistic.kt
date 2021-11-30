package com.example.colorimagemobile.ui.home.fragments.accountParameter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.users.UserService


class UserStatistic : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_user_statistic, container, false)

        inf.findViewById<TextView>(R.id.nbCollab).text = UserService.getUserInfo().collaborationHistory.size.toString()
        inf.findViewById<TextView>(R.id.nbDrawings).text = UserService.getUserInfo().drawings.size.toString()
        inf.findViewById<TextView>(R.id.nbTeams).text = UserService.getUserInfo().teams.size.toString()
        inf.findViewById<TextView>(R.id.Time).text = UserService.getUserInfo().teams.size.toString()

        return inf
    }


}