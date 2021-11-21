package com.example.colorimagemobile.classes.xml_json

import com.example.colorimagemobile.classes.JSONConvertor
import com.github.underscore.lodash.U
import org.json.JSONObject

class SVGParser<T>(svgAsString: String, classType: Class<T>) {
    private var svgAsString = svgAsString
    private var svgClass: T? = null
    private val classType = classType

    init {
        parseIntoCustomSVG()
    }

    // converts string to { svg: {} }
    fun getJSON(): String {
        return U.xmlToJson(svgAsString)
    }

    // convert json to <svg></svg>
    fun getXML(): String {
        return U.jsonToXml(getJSON())
    }

    // remove useless substrings or modify string
    private fun modifyExtras(): String {
        var svgString = getJSON()
        svgString = svgString.replace("\"-", "\"")

        return svgString
    }

    // convert the string JSON to our own model
    private fun parseIntoCustomSVG() {
        val svgString = modifyExtras()
        val wholeSVGJson = JSONObject(svgString).getString("svg")
        val svgJSON = JSONObject(wholeSVGJson)

        svgClass = JSONConvertor.getJSONObject(svgJSON.toString(), classType) as T
    }

    fun getCustomSVG(): T {
        return svgClass as T
    }

    fun getBackgroundColor(style: String): String {
        return style.replace("background-color: ", "")
    }
}