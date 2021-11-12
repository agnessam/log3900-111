package com.example.colorimagemobile.services.drawing.toolsAttribute

import android.graphics.Color
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

object ColorService {
    private var currentColor: String = "rgba(0, 0, 0, 1)"

    fun setColor(newColor: String) {
        currentColor = newColor
    }

    fun getColorAsString(): String {
        return currentColor
    }

    fun getColorAsInt(): Int {
        return rgbaToInt(currentColor)
    }

    // convert rgb() to Android Color in Integer
    fun rgbaToInt(color: String): Int {
        val rgbValues = color.substring(color.indexOf('(') + 1, color.indexOf(')'))
        val splitRGB = rgbValues.replace("\\s".toRegex(),"").split(",")
        val alpha = if (splitRGB.size == 4) splitRGB[3].toInt() else 255

        printMsg(alpha.toString())

        return Color.argb(alpha, splitRGB[0].toInt(), splitRGB[1].toInt(), splitRGB[2].toInt())
    }

    fun intToRGB(color: Int): String {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val alpha = Color.alpha(color)

        return "rgba($red, $green, $blue, $alpha)"
    }
}