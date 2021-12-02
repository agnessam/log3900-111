package com.example.colorimagemobile.classes.xml_json

import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.models.*
import com.github.underscore.lodash.U
import org.json.JSONArray
import org.json.JSONObject

class SVGParser<T>(svgAsString: String, classType: Class<T>) {
    private var svgJSONString = JSONObject(U.xmlToJson(svgAsString).replace("\"-", "\"")).getString("svg")
    private var svgJSON = JSONObject(svgJSONString)
    private var svgClass: T? = null

    init {
        convertObjectsToList()
        svgClass = JSONConvertor.getJSONObject(svgJSONString, classType) as T
    }

    fun String.insert(index: Int, string: String): String {
        return this.substring(0, index) + string + this.substring(index, this.length)
    }

    private fun convertObjectsToList(){
        checkObjectList("rect")
        checkObjectList("ellipse")
        checkObjectList("polyline")
        svgJSONString = svgJSON.toString()
    }

    // if shape has {} format, change to [{}]
    private fun checkObjectList(shape: String) {
        if (!svgJSON.has(shape)) return

        if(svgJSON[shape] !is JSONArray) {
            svgJSON.remove(shape)
            return
        }

        var shapeClass = when(shape){
            "rect" -> Rectangle::class.java
            "ellipse" -> Ellipse::class.java
            "polyline" -> Polyline::class.java
            else -> return
        }

        var shapeJSONHashMap = HashMap<String, Shape>()

        for(i in 0 until (svgJSON[shape] as JSONArray).length()){
            val shapeJSON = ((svgJSON[shape] as JSONArray)[i]) as JSONObject
            val shapeObject = JSONConvertor.getJSONObject(shapeJSON.toString(), shapeClass)
            val id = shapeObject.id
            shapeJSONHashMap[id] = shapeObject
        }
        svgJSON.remove(shape)
        svgJSON.put(shape, JSONConvertor.convertToJSON(shapeJSONHashMap))
    }

    fun getCustomSVG(): T {
        return svgClass as T
    }

    fun getBackgroundColor(style: String): String {
        return style.replace("background-color: ", "")
    }
}