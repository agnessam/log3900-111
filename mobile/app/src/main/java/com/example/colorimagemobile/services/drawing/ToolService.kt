package com.example.colorimagemobile.services.drawing

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.R
import com.example.colorimagemobile.ui.home.fragments.drawing.attributes.eraser.EraserFragment
import com.example.colorimagemobile.ui.home.fragments.drawing.attributes.pencil.PencilFragment

data class ToolsMap(val index: Int, val toolName: String, val icon: Int, val fragment: Fragment)

/**
 * @todo: Add dynamic class View as param
 */
enum class ToolType(val index: Int, val toolName: String, val icon: Int, val fragment: Fragment) {
    PENCIL(0, "Pencil", R.drawable.drawing_icon_pencil, PencilFragment()),
    ERASER(1, "Eraser", R.drawable.drawing_icon_eraser, EraserFragment()),
}

object ToolService {
    private var currentTool: MutableLiveData<ToolsMap>
    private var tools: ArrayList<ToolsMap> = arrayListOf()

    init {
        enumValues<ToolType>().forEach {
            tools.add(ToolsMap(it.index, it.toolName, it.icon, it.fragment))
        }

        this.currentTool = MutableLiveData()
        this.setCurrentTool(0)
    }

    fun getCurrentTool(): LiveData<ToolsMap> {
        return this.currentTool
    }

    fun getCurrentToolValue(): ToolsMap? {
        return this.currentTool.value
    }

    fun setCurrentTool(index: Int) {
        this.currentTool.value = tools[index]
    }

    fun getAllTools(): ArrayList<ToolsMap> {
        return this.tools
    }
}