package com.example.colorimagemobile.services.drawing.toolsAttribute

import android.graphics.Color

object ColorService {
    private var currentColor: String = "#000000"

    fun setColorAsString(newColor: Int) {
        currentColor = String.format("#%06X", 0xFFFFFF and newColor)
    }

    fun getColorAsString(): String {
        return currentColor
    }

    fun getColorAsInt(): Int {
        return Color.parseColor(currentColor)
    }
}