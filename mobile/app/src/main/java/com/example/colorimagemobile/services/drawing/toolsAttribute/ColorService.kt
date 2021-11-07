package com.example.colorimagemobile.services.drawing.toolsAttribute

import android.graphics.Color

object ColorService {
    private var currentColor: Int = Color.BLACK

    fun setColor(newColor: Int) {
        currentColor = newColor
    }

    fun getColor(): Int {
        return currentColor
    }
}