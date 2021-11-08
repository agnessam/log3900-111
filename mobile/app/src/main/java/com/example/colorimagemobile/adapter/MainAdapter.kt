package com.example.colorimagemobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.Interface.OnItemClick
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.ToolModel
import java.util.*

// adapter for tools
class MainAdapter(toolModelArrayList: ArrayList<ToolModel>, var context: Context, private val onItemClick: OnItemClick?) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    var toolModelArrayList: ArrayList<ToolModel>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.main_adapter_items, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.mainItemsImage.setImageResource(toolModelArrayList[position].mToolIcon)
        holder.txtTool.setText(toolModelArrayList[position].mToolName)
        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return toolModelArrayList.size
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mainItemsImage: ImageView
        var txtTool: TextView

        init {
            mainItemsImage = itemView.findViewById(R.id.imgToolIcon)
            txtTool = itemView.findViewById(R.id.txtTool)
        }
    }

    init {
        this.toolModelArrayList = toolModelArrayList
    }

}