package com.example.colorimagemobile.ui.home.fragments.userProfile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.CollaborationHistoryRecyclerAdapter
import com.example.colorimagemobile.models.CollaborationHistory
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.Constants
import kotlinx.android.synthetic.main.fragment_user_profile_history.*


class UserProfileHistoryFragment : Fragment() , CollaborationHistoryRecyclerAdapter.OnItemClickListener{
    private lateinit  var layoutManagerCollaboration : RecyclerView.LayoutManager
    private  var adapterCollaboration: RecyclerView.Adapter<CollaborationHistoryRecyclerAdapter.ViewHolder>?=null

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

        // set value for login and logout
        inf.findViewById<TextView>(R.id.userLastLogin).text = UserService.getUserInfo().lastLogin.toString()
        inf.findViewById<TextView>(R.id.userlastLogout).text = UserService.getUserInfo().lastLogout.toString()

        return inf
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManagerCollaboration = LinearLayoutManager(context)
        collabHistoryRecyclerView.layoutManager = layoutManagerCollaboration
        adapterCollaboration = CollaborationHistoryRecyclerAdapter(this)
        collabHistoryRecyclerView.adapter = adapterCollaboration
    }


    override fun onItemClick(position: Int) {
        val clickedItem: CollaborationHistory.drawingHistory= UserService.getUserInfo().collaborationHistory[position]
        UserService.setCurrentCollaborationHistory(clickedItem)
    }

}

