package com.example.colorimagemobile.services.drawing

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import com.example.colorimagemobile.classes.CommandFactory
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.classes.xml_json.SVGParser
import com.example.colorimagemobile.classes.xml_json.StringParser
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.CustomSVG
import com.example.colorimagemobile.models.PencilData
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

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

    fun getDrawable(layerIndex: Int): Drawable{
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

        // 2. Create canvas
        CanvasService.setWidth(svgObject.width.toInt())
        CanvasService.setHeight(svgObject.height.toInt())

        CanvasService.createNewBitmap()
        val backgroundColor = svgParser.getBackgroundColor(svgObject.style)
        CanvasService.updateCanvasColor(ColorService.rgbaToInt(backgroundColor))

        printMsg(svgObject.polyline.toString())

        // create pencil data for example
        svgObject.polyline?.forEach { pencil ->
            if (pencil.id.isNullOrEmpty()) return@forEach

            val style = StringParser.getStyles(pencil.style)

            // 3. get style
            val pencilData = PencilData(
                id = pencil.id,
                fill = "none",
                stroke = style.stroke,
                fillOpacity = "none",
                strokeOpacity = ColorService.convertOpacityToAndroid(style.strokeOpacity.toString()).toString(),
                strokeWidth = style.strokeWidth,
                pointsList = arrayListOf(),
            )

            val points = pencil.points.split(",")

            val firstPoint = points[0].split(" ")
            pencilData.pointsList.add(Point(firstPoint[0].toFloat(), firstPoint[1].toFloat()))

            val command = CommandFactory.createCommand("Pencil", pencilData) as PencilCommand
            command.execute()

            points.forEach { point ->
                val splicedPoint = point.split(" ")
                command.addPoint(splicedPoint[0].toFloat(), splicedPoint[1].toFloat())
                command.execute()
            }
        }
    }
}