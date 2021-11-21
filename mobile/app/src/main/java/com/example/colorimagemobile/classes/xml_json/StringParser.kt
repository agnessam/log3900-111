package com.example.colorimagemobile.classes.xml_json

import com.example.colorimagemobile.models.SvgStyle

class StringParser {

    companion object {

        private fun removePX(value: String): Int {
            val pxValue = value.replace("\\s".toRegex(),"")
            return pxValue.dropLast(2).toInt()
        }

        fun getStyles(styleString: String): SvgStyle {
            val svgStyle: SvgStyle = SvgStyle()

            styleString.split(";").forEach { slicedStyle ->
                val style = slicedStyle.split(":")

                if (style.size == 2) {
                    val value = style[1]

                    when (style[0]) {
                        "stroke-width" -> svgStyle.strokeWidth = removePX(value)
                        "fill" -> svgStyle.fill = value
                        "stroke" -> svgStyle.stroke = value
                        "stroke-opacity" -> svgStyle.strokeOpacity = 255 // to change
                    }
                }
            }

            return svgStyle
        }
    }
}