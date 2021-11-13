package com.example.colorimagemobile.classes.xml_json

import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.models.CustomSVG
import com.github.underscore.lodash.U
import org.json.JSONObject

class SVGParser(svgAsString: String) {
    private val svgAsString = svgAsString
    private lateinit var customSVG: CustomSVG

    init {
        modifyExtras()
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

        customSVG = JSONConvertor.getJSONObject(svgJSON.toString(), CustomSVG::class.java)
    }

    fun getCustomSVG(): CustomSVG {
        return customSVG
    }

    fun getBackgroundColor(): String {
        return customSVG.style.replace("background-color: ", "")
    }
}