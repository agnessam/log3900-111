package com.example.colorimagemobile.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.classes.MyPicasso
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.ui.home.fragments.users.UsersProfileFragment
import com.example.colorimagemobile.utils.Constants

class MembersListRecyclerAdapter(
    private val fragmentActivity: FragmentActivity,
    private val members: ArrayList<UserModel.AllInfo>,
    private val userStatus: HashMap<String, UserModel.STATUS>,
    private val closeSheet: () -> Unit
):
    RecyclerView.Adapter<MembersListRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersListRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_member, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MembersListRecyclerAdapter.ViewHolder, position: Int) {
        holder.memberName.text = members[position].username
        holder.status.setCardBackgroundColor(getStatusColor(members[position]._id))
        holder.statusText.text = userStatus[members[position]._id]?.status
        MyPicasso().loadImage(members[position].avatar.imageUrl, holder.memberImage)
    }

    override fun getItemCount(): Int = members.size

    private fun getStatusColor(memberId: String): Int {
        val color = when(userStatus[memberId]) {
            UserModel.STATUS.Online -> "#0ff00"
            UserModel.STATUS.Offline -> "#ff0000"
            else -> "#f4b6b0"
        }

        return Color.parseColor(color)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var memberName : TextView = itemView.findViewById(R.id.card_member_username);
        var memberImage : ImageView = itemView.findViewById(R.id.card_member_imageview);
        val status: CardView = itemView.findViewById(R.id.card_member_status)
        var statusText: TextView = itemView.findViewById(R.id.card_member_status_text)

        init {
            itemView.setOnClickListener {
                closeSheet()
                val userId = members[bindingAdapterPosition]._id
                MyFragmentManager(fragmentActivity).openWithData(R.id.teamsMenuFrameLayout, UsersProfileFragment(), Constants.USERS.CURRENT_USER_ID_KEY, userId)
            }
        }
    }
}