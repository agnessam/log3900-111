package com.example.colorimagemobile.ui.PublicProfile

//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import com.example.colorimagemobile.R
//import com.example.colorimagemobile.adapter.UsersMenuRecyclerAdapter
//import com.example.colorimagemobile.models.UserModel
//import com.example.colorimagemobile.services.users.UserService
//import com.example.colorimagemobile.utils.CommonFun
//import com.example.colorimagemobile.utils.Constants
//
//class PublicProfileFragment : Fragment(), UsersMenuRecyclerAdapter.OnItemClickListener {
//    private lateinit var posts : TextView
//    private lateinit var followers : TextView
//    private lateinit var following : TextView
//    private lateinit var description : TextView
//    private lateinit var username : TextView
//    private lateinit var avatar : ImageView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        val inf =inflater.inflate(R.layout.fragment_public_profile, container, false)
//
//        posts = inf.findViewById<View>(R.id.editTextNumberPosts) as TextView
//        followers = inf.findViewById<View>(R.id.editTextNumberFollowers) as TextView
//        following = inf.findViewById<View>(R.id.editTextNumberFollowing) as TextView
//        description = inf.findViewById<View>(R.id.publicDescription) as TextView
//        username = inf.findViewById<View>(R.id.userdisplayNameText) as TextView
//        avatar = inf.findViewById<View>(R.id.publicProfile) as ImageView
//
//
//
//        return inf
//    }
//
//    // on item select set user/team profile
//    override fun onItemClick(position: Int) {
//        val clickedItem: UserModel.AllInfo = UserService.getAllUserInfo()[position]
//        UserService.setSelectUser(clickedItem)
//        val currentInfo = UserService.getSelectUser()
//
//        // sets the derived data
//        posts.text = Constants.EMPTY_STRING
//        followers.text = Constants.EMPTY_STRING
//        following.text = Constants.EMPTY_STRING
//        description.text = currentInfo.description
//        username.text = currentInfo.username
//        CommonFun.loadUrl(currentInfo.avatar.imageUrl, avatar)
//    }

//}