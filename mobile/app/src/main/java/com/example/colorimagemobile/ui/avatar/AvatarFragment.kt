package com.example.colorimagemobile.ui.avatar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.AvatarRecyclerAdapter
import com.example.colorimagemobile.models.AvatarModel
import com.example.colorimagemobile.repositories.AvatarRepository
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.services.avatar.AvatarService
import kotlinx.android.synthetic.main.fragment_avatar.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AvatarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AvatarFragment : Fragment() {
    private lateinit var layoutManagerAvatar : RecyclerView.LayoutManager
    private lateinit var adapterAvatar: RecyclerView.Adapter<AvatarRecyclerAdapter.ViewHolder>

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_avatar, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AvatarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AvatarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllAvatar()
    }

    private fun getAllAvatar(){
        AvatarRepository().getAllAvatar(UserService.getToken()).observe(context as LifecycleOwner,{
            if (it.isError as Boolean) {
            return@observe
        }
        val avatars = it.data as ArrayList<AvatarModel.AllInfo>
            AvatarService.setAvatars(avatars)
            setRecyclerView()

        })

    }

    private fun setRecyclerView() {
        layoutManagerAvatar = LinearLayoutManager(context)
        recyclerViewAvatar.layoutManager = layoutManagerAvatar
        adapterAvatar = AvatarRecyclerAdapter()
        recyclerViewAvatar.adapter = adapterAvatar
    }

}