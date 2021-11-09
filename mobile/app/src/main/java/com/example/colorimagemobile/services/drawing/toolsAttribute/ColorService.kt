package com.example.colorimagemobile.services.drawing.toolsAttribute

import android.graphics.Color
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

object ColorService {
    private var currentColor: String = "#000000"

    fun convertColorToInt(colorToConvert: String): Int {
        return Color.parseColor(colorToConvert)
    }

    fun setColorAsString(newColor: Int) {
        currentColor = String.format("#%06X", 0xFFFFFF and newColor)
    }

    fun getColorAsString(): String {
        return currentColor
    }

    fun getColorAsInt(): Int {
        return convertColorToInt(currentColor)
    }

    // convert rgb() to Android Color in Integer
    fun rgbToInt(color: String): Int {
        val rgbValues = color.substring(color.indexOf('(') + 1, color.indexOf(')'))
        val splitRGB = rgbValues.split(",")

        return Color.rgb(splitRGB[0].toInt(), splitRGB[1].toInt(), splitRGB[2].toInt())
    }
}