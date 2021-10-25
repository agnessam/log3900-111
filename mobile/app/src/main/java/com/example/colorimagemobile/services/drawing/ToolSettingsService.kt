package com.example.colorimagemobile.services.drawing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object ToolSettingsService {
    lateinit var currentWidth: MutableLiveData<Int>

    init {
        reset()
    }

    fun setCurrentWidth(newWidth: Int) {
        this.currentWidth.value = newWidth
    }

    fun getCurrentWidth(): LiveData<Int> {
        return this.currentWidth
    }

    fun reset() {
        this.currentWidth = MutableLiveData()
        this.currentWidth.value = 15
    }
}