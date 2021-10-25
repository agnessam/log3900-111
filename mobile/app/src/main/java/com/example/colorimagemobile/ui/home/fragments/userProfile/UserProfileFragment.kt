package com.example.colorimagemobile.ui.home.fragments.userProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.R

class UserProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val showUserProfile = ShowUserProfile()
        parentFragmentManager.beginTransaction().replace(R.id.newLayout, showUserProfile)
            .commit()


        view.findViewById<TextView>(R.id.parametredecompt).setOnClickListener {
            val showUserProfile = ShowUserProfile()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, showUserProfile)
                .commit()

        }
        view.findViewById<TextView>(R.id.editer_profili).setOnClickListener {
            val editProfileFragment = EditProfileFragment()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, editProfileFragment)
                .commit()

        }
        view.findViewById<TextView>(R.id.motdepasse).setOnClickListener {
            val motdePasseFragment = MotdePasseFragment()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, motdePasseFragment)
                .commit()

        }
        view.findViewById<TextView>(R.id.historique).setOnClickListener {
            val history = UserProfileHistory()
            parentFragmentManager.beginTransaction().replace(R.id.newLayout, history)
                .commit()


        }

    }

}