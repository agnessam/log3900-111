package com.example.colorimagemobile.services.drawing.toolsAttribute

import kotlin.math.abs

object PencilService {
    const val minWidth = 1
    const val maxWidth = 40
    private var currentWidth: Int

    init {
        // default is the middle number of min and max
        currentWidth = abs(
            (minWidth - maxWidth)/2
        )
    }

    fun setCurrentWidth(newWidth: Int) {
        currentWidth = newWidth
    }

    fun getCurrentWidth(): Int {
        return currentWidth
    }

    fun getCurrentWidthAsFloat(): Float {
        return currentWidth.toFloat()
    }
}