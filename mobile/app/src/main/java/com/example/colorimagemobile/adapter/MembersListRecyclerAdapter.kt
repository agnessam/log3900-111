package com.example.colorimagemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyPicasso
import com.example.colorimagemobile.models.UserModel

class MembersListRecyclerAdapter(
    private val members: ArrayList<UserModel.AllInfo>
):
    RecyclerView.Adapter<MembersListRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersListRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_member, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MembersListRecyclerAdapter.ViewHolder, position: Int) {
        holder.memberName.text = members[position].username
        MyPicasso().loadImage(members[position].avatar.imageUrl, holder.memberImage)
    }

    override fun getItemCount(): Int = members.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var memberName : TextView = itemView.findViewById(R.id.card_member_username);
        var memberImage : ImageView = itemView.findViewById(R.id.card_member_imageview);

        init {
            itemView.setOnClickListener {
                val userId = members[bindingAdapterPosition]._id
            }
        }
    }
}