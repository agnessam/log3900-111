package com.example.colorimagemobile.services.drawing

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.classes.JSONConvertor
import com.example.colorimagemobile.classes.openDrawingCommand.CreateEllipseCommand
import com.example.colorimagemobile.classes.openDrawingCommand.CreatePolylineCommand
import com.example.colorimagemobile.classes.openDrawingCommand.CreateRectangleCommand
import com.example.colorimagemobile.classes.xml_json.SVGBuilder
import com.example.colorimagemobile.classes.xml_json.SVGParser
import com.example.colorimagemobile.classes.xml_json.shapes
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.CustomSVG
import com.example.colorimagemobile.models.Ellipse
import com.example.colorimagemobile.models.Polyline
import com.example.colorimagemobile.models.Rectangle
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.github.underscore.lodash.U
import org.json.JSONObject
import org.json.XML
import java.lang.String.join

object DrawingObjectManager {
    private var layerDrawable: LayerDrawable = LayerDrawable(arrayOf<Drawable>())
    private var commandList: HashMap<String,ICommand> = HashMap()
    private var layerIdUuidMap: HashMap<Int, String> = HashMap()

    var numberOfLayers: Int = 0
        get() = layerDrawable.numberOfLayers


    fun draw(canvas: Canvas){
        layerDrawable.draw(canvas)
    }

    fun setDrawable(layerIndex:Int, drawable:Drawable){
        layerDrawable.setDrawable(layerIndex, drawable)
    }

    fun addLayer(drawable: Drawable, uuid: String): Int{
        var layerId = layerDrawable.addLayer(drawable)
        layerIdUuidMap[layerId] = uuid
        return layerId
    }

    fun getDrawable(layerIndex: Int): Drawable?{
        if(layerIndex >= layerDrawable.numberOfLayers)
            return null
        return layerDrawable.getDrawable(layerIndex)
    }

    fun getLayerIndex(uuid: String): Int {
        return layerIdUuidMap.filterValues { it == uuid }.keys.first()
    }

    fun getUuid(layerIndex: Int): String? {
        return layerIdUuidMap[layerIndex]
    }

    fun addCommand(uuid:String, command: ICommand){
        commandList[uuid] =  command
    }

    fun getCommand(uuid: String): ICommand?{
        return commandList[uuid]
    }

    fun getCommand(layerIndex: Int): ICommand? {
        return commandList[layerIdUuidMap[layerIndex]]
    }

    fun removeCommand(layerIndex: Int) {
        commandList.remove(layerIdUuidMap[layerIndex])
    }

    fun clearLayers() {
        layerDrawable = LayerDrawable(arrayOf<Drawable>())
        commandList.clear()
        layerIdUuidMap.clear()
    }

    fun createDrawableObjects(base64: String) {
        CanvasService.createNewBitmap()

        // 1. init svg
        val svgParser = SVGParser(base64, CustomSVG::class.java)
        val svgObject = svgParser.getCustomSVG()

        // 2. init canvas properties and create it
        CanvasService.setWidth(svgObject.width.toInt())
        CanvasService.setHeight(svgObject.height.toInt())

        CanvasService.createNewBitmap()
        val backgroundColor = svgParser.getBackgroundColor(svgObject.style)
        CanvasService.updateCanvasColor(ColorService.rgbaToInt(backgroundColor))

        if(svgObject.shapes != null) {
            for(shape in svgObject.shapes!!){
                when(shape){
                    is Polyline -> {
                        val createPolylineCommand = CreatePolylineCommand(shape)
                        createPolylineCommand.execute()
                    }
                    is Rectangle -> {
                        val createRectangleCommand = CreateRectangleCommand(shape)
                        createRectangleCommand.execute()
                    }
                    is Ellipse -> {
                        val createEllipseCommand = CreateEllipseCommand(shape)
                        createEllipseCommand.execute()
                    }
                }
            }
        }

        // 4. store our "json" object containing every shapes
        DrawingJsonService.setSVGObject(svgObject)
    }

    // converts drawables to svg/xml and then converts to base64
    fun getDrawingDataURI(): String {
        val svgObject = DrawingJsonService.getSvgObject()!!

        var rootSvgString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"${svgObject.width}\" height=\"${svgObject.height}\" style=\"${svgObject.style}\">"

        if (!svgObject.shapes.isNullOrEmpty()) {

            svgObject.shapes!!.forEach{ shape ->
                if (shape.id == null || shape.id == "") return@forEach
                when(shape){
                    is Polyline -> {
                        val propertyValuePairs: ArrayList<Pair<String, String>> = arrayListOf(
                            Pair("id", shape.id),
                            Pair("name", shape.name),
                            Pair("points", shape.points),
                            Pair("style", shape.style),
                            Pair("transform", if(shape.transform == null) "" else shape.transform)
                        )
                        val propertiesJoinString = propertyValuePairs.joinToString(separator = " ") { it -> " ${it.first}=\"${it.second}\"" }
                        rootSvgString += "<polyline $propertiesJoinString ></polyline>"
                    }
                    is Rectangle -> {
                        val propertyValuePairs: ArrayList<Pair<String, String>> = arrayListOf(
                            Pair("id", shape.id),
                            Pair("name", shape.name),
                            Pair("x", shape.x),
                            Pair("y", shape.y),
                            Pair("width", shape.width),
                            Pair("height", shape.height),
                            Pair("style", shape.style),
                            Pair("transform", if(shape.transform == null) "" else shape.transform)
                        )
                        val propertiesJoinString = propertyValuePairs.joinToString(separator = " ") { it -> "${it.first}=\"${it.second}\"" }
                        rootSvgString += "<rect $propertiesJoinString ></rect>"
                    }
                    is Ellipse -> {
                        val propertyValuePairs: ArrayList<Pair<String, String>> = arrayListOf(
                            Pair("id", shape.id),
                            Pair("name", shape.name),
                            Pair("cx", shape.cx),
                            Pair("cy", shape.cy),
                            Pair("rx", shape.rx),
                            Pair("ry", shape.ry),
                            Pair("width", shape.width),
                            Pair("height", shape.height),
                            Pair("style", shape.style),
                            Pair("transform", if(shape.transform == null) "" else shape.transform)
                        )
                        val propertiesJoinString = propertyValuePairs.joinToString(separator = " ") { it -> " ${it.first}=\"${it.second}\"" }
                        rootSvgString += "<ellipse $propertiesJoinString ></ellipse>"
                    }
                }
            }
        }
        rootSvgString += "</svg>"
        return ImageConvertor.XMLToBase64(rootSvgString)
    }
}