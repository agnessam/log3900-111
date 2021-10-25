package com.example.colorimagemobile.services.drawing.toolsAttribute

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.abs

object PencilService {
    const val MIN_VALUE = 1
    const val MAX_VALUE = 40
    private var currentWidth: MutableLiveData<Int> = MutableLiveData()

    init {
        // default is the middle number of min and max
        currentWidth.value = abs(
            (MAX_VALUE - MIN_VALUE)/2
        )
    }

    fun setCurrentWidth(newWidth: Int) {
        currentWidth.value = newWidth
    }

    fun getCurrentWidth(): LiveData<Int> {
        return currentWidth
    }

}