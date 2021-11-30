package com.example.colorimagemobile.ui.home.fragments.accountParameter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.Constants


class UserStatistic : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inf = inflater.inflate(R.layout.fragment_user_statistic, container, false)

        //set value for statistics
        inf.findViewById<TextView>(R.id.nbCollab).text = UserService.getStat().numberOfCollaborations.toString()
        inf.findViewById<TextView>(R.id.nbDrawings).text = UserService.getStat().numberOfDrawings.toString()
        inf.findViewById<TextView>(R.id.nbTeams).text = UserService.getStat().numberOfTeams.toString()
        val averageTime = String.format("%.2f",UserService.getStat().averageCollaborationTime)+Constants.MINUTES
        inf.findViewById<TextView>(R.id.Time).text = averageTime
        return inf
    }

}