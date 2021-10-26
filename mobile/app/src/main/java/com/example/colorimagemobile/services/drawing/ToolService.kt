package com.example.colorimagemobile.services.drawing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.interfaces.IToolCommand

object ToolService {
    private var currentTool: MutableLiveData<IToolCommand> = MutableLiveData()
    private var tools: ArrayList<IToolCommand> = arrayListOf()

    init {
        // add every tool in array
        enumValues<ToolType>().forEach {
            tools.add(it.command)
        }

        this.setCurrentTool(ToolType.PENCIL)
    }

    fun getCurrentTool(): LiveData<IToolCommand> {
        return this.currentTool
    }

    fun setCurrentTool(toolType: ToolType) {
        this.currentTool.value = toolType.command
    }

    fun getAllTools(): ArrayList<IToolCommand> {
        return this.tools
    }
}