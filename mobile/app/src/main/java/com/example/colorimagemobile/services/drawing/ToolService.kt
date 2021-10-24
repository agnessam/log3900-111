package com.example.colorimagemobile.services.drawing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.R

data class ToolsMap(val index: Int, val tool: String, val icon: Int)

/**
 * @todo: Add dynamic class View as param
 */
enum class ToolType(val index: Int, val tool: String, val icon: Int) {
    PENCIL(0, "Pencil", R.drawable.drawing_icon_pencil),
    ERASER(1, "Eraser", R.drawable.drawing_icon_eraser),
}

object ToolService {
    private var currentTool: MutableLiveData<ToolsMap>
    private var tools: ArrayList<ToolsMap> = arrayListOf()

    init {
        enumValues<ToolType>().forEach {
            tools.add(ToolsMap(it.index, it.tool, it.icon))
        }

        this.currentTool = MutableLiveData()
        this.setCurrentTool(0)
    }

    fun getCurrentTool(): LiveData<ToolsMap> {
        return this.currentTool
    }

    fun setCurrentTool(index: Int) {
        this.currentTool.value = tools[index]
    }

    fun getAllTools(): ArrayList<ToolsMap> {
        return this.tools
    }
}