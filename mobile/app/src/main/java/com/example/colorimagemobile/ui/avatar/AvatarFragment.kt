package com.example.colorimagemobile.ui.avatar

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.AvatarRecyclerAdapter
import kotlinx.android.synthetic.main.card_default_avatar.*
import kotlinx.android.synthetic.main.fragment_avatar.*
import kotlinx.android.synthetic.main.fragment_avatar.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [AvatarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AvatarFragment : FrameLayout {
    private  var layoutManagerAvatar : RecyclerView.LayoutManager? = null
    private  var adapterAvatar: RecyclerView.Adapter<AvatarRecyclerAdapter.ViewHolder>?=null


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        inflate(context, R.layout.fragment_avatar, this)
        layoutManagerAvatar = GridLayoutManager(context,3)
        recyclerViewAvatar.layoutManager = layoutManagerAvatar
        adapterAvatar = AvatarRecyclerAdapter()
        recyclerViewAvatar.adapter = adapterAvatar
    }



}