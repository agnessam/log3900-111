package com.example.colorimagemobile.classes.xml_json

import com.example.colorimagemobile.classes.JSONConvertor
import org.json.JSONArray
import org.json.JSONObject
import org.json.XML

val shapes = arrayOf("rect", "polyline", "ellipse")
class SVGParser<T>(xmlString: String, private val classType: Class<T>) {
    private var svgClass: T? = null
    private val svgJSON: JSONObject = (XML.toJSONObject(xmlString))["svg"] as JSONObject

    init {
        for(shape in shapes) checkObjectList(shape)
        svgClass = JSONConvertor.getJSONObject(svgJSON.toString(), classType) as T
    }

    fun String.insert(index: Int, string: String): String {
        return this.substring(0, index) + string + this.substring(index, this.length)
    }

    // if shape has {} format, change to [{}]
    private fun checkObjectList(shape: String){
        if(!svgJSON.has(shape)) return

        if(svgJSON[shape] !is JSONArray){
            var jsonObject = svgJSON.getJSONObject(shape)
            var newJSONArray = JSONArray().put(jsonObject)
            svgJSON.remove(shape)
            svgJSON.put(shape, newJSONArray)
        }
    }

    fun getCustomSVG(): T {
        return svgClass as T
    }

    fun getBackgroundColor(style: String): String {
        return style.replace("background-color: ", "")
    }
}