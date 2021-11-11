package com.example.colorimagemobile.ui.userProfile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants


class UserProfileHistoryFragment : Fragment() {
    private val userRepository: UserRepository = UserRepository()


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
        val lastLogin= inf.findViewById<View>(R.id.lastLogin) as TextView
        val lastLogout = inf.findViewById<View>(R.id.lastLogout) as TextView

        // get log history
        val log : ArrayList<String> = UserService.getLogHistory()

       // sets the derived data in the textView
        lastLogin.text = log.get(0)
        lastLogout.text =log.get(1)
        printMsg("before get user")

        return inf
    }



}

