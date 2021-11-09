package com.example.colorimagemobile.services.drawing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object CanvasUpdateService {

    private val updateCanvas: MutableLiveData<Boolean> = MutableLiveData()

    fun invalidate() {
        updateCanvas.value = true
    }

    fun deactivate() {
        updateCanvas.value = false
    }

    fun getLiveData(): LiveData<Boolean> {
        return updateCanvas
    }
}