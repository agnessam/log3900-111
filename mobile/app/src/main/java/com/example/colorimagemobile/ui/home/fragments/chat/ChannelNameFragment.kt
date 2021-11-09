package com.example.colorimagemobile.ui.home.fragments.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.ChatChannelRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_channel_name.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ChannelNameFragment : Fragment() {

    private var layoutManager : RecyclerView.LayoutManager?=null
    private  var adapter: RecyclerView.Adapter<ChatChannelRecyclerAdapter.ViewHolder>?=null

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
        return inflater.inflate(R.layout.fragment_channel_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(getContext())
        recyclerView.layoutManager = layoutManager
        adapter = ChatChannelRecyclerAdapter()
        recyclerView.adapter = adapter
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChannelNameFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}