package com.example.colorimagemobile.services.drawing.toolsAttribute

import android.graphics.Color

object ColorService {
    private var primaryColor: String = "rgba(0, 0, 0, 1)"
    private var secondaryColor: String = "rgba(0, 0, 0, 1)"

    fun setPrimaryColor(newColor: String) {
        primaryColor = newColor
    }

    fun setSecondaryColor(newColor: String){
        secondaryColor = newColor
    }

    fun getPrimaryColorAsString(): String {
        return primaryColor
    }

    fun getSecondaryColorAsString(): String {
        return secondaryColor
    }

    fun getPrimaryColorAsInt(): Int {
        return rgbaToInt(primaryColor)
    }

    fun getSecondaryColorAsInt(): Int{
        return rgbaToInt(secondaryColor)
    }

    // convert rgb() to Android Color in Integer
    fun rgbaToInt(color: String): Int {
        val rgbValues = color.substring(color.indexOf('(') + 1, color.indexOf(')'))
        val splitRGB = rgbValues.replace("\\s".toRegex(),"").split(",")
        val alpha = if (splitRGB.size == 4) splitRGB[3].toInt() else 255

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