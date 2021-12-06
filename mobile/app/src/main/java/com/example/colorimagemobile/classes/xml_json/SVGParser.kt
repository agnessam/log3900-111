package com.example.colorimagemobile.classes.xml_json

import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.models.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.XML

val shapes = arrayOf("rect", "polyline", "ellipse")
val SHAPES_JSON_TAG = "shapes"
class SVGParser<T>(xmlString: String, private val classType: Class<T>) {
    private var svgClass: T? = null
    private val svgJSON: JSONObject

    init {
        if(classType == CustomSVG::class.java){
            var tagModifiedXmlString = xmlString
            for(shape in shapes){
                tagModifiedXmlString = tagModifiedXmlString.replace("<$shape", "<$SHAPES_JSON_TAG")
                tagModifiedXmlString = tagModifiedXmlString.replace("/$shape>", "/$SHAPES_JSON_TAG>")
            }
            svgJSON = (XML.toJSONObject(tagModifiedXmlString))["svg"] as JSONObject
            if(svgJSON.has(SHAPES_JSON_TAG)) {
                if(svgJSON[SHAPES_JSON_TAG] is JSONObject){
                    val newJSONArray = JSONArray().put(svgJSON[SHAPES_JSON_TAG])
                    svgJSON.remove(SHAPES_JSON_TAG)
                    svgJSON.put(SHAPES_JSON_TAG, newJSONArray)
                }

                var shapeArray: ArrayList<Shape> = ArrayList()
                for(i in 0 until (svgJSON[SHAPES_JSON_TAG] as JSONArray).length()){
                    val shapeJSON = (svgJSON[SHAPES_JSON_TAG] as JSONArray)[i] as JSONObject
                    if(!shapeJSON.has("name")) continue
                    val shapeClass = when(shapeJSON["name"]){
                        "rectangle" ->  Rectangle::class.java
                        "ellipse" -> Ellipse::class.java
                        "pencil" -> Polyline::class.java
                        else -> null
                    }
                    shapeClass ?: continue
                    val shape = JSONConvertor.getJSONObject(shapeJSON.toString(), shapeClass)
                    shapeArray.add(shape)
                }
                svgJSON.remove(SHAPES_JSON_TAG)
                svgClass = JSONConvertor.getJSONObject(svgJSON.toString(), classType) as T
                (svgClass as CustomSVG).shapes = shapeArray
            }
            else{
                svgClass = JSONConvertor.getJSONObject(svgJSON.toString(), classType) as T
            }
        }
        else {
            svgJSON = (XML.toJSONObject(xmlString))["svg"] as JSONObject
            svgClass = JSONConvertor.getJSONObject(svgJSON.toString(), classType) as T
        }

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