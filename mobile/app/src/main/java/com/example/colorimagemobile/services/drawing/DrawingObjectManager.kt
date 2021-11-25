package com.example.colorimagemobile.services.drawing

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.classes.openDrawingCommand.CreateEllipseCommand
import com.example.colorimagemobile.classes.openDrawingCommand.CreatePolylineCommand
import com.example.colorimagemobile.classes.openDrawingCommand.CreateRectangleCommand
import com.example.colorimagemobile.classes.xml_json.SVGBuilder
import com.example.colorimagemobile.classes.xml_json.SVGParser
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.CustomSVG
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.github.underscore.lodash.U

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
        DrawingJsonService.setSVGObject(svgObject)

        // 2. init canvas properties and create it
        CanvasService.setWidth(svgObject.width.toInt())
        CanvasService.setHeight(svgObject.height.toInt())

        CanvasService.createNewBitmap()
        val backgroundColor = svgParser.getBackgroundColor(svgObject.style)
        CanvasService.updateCanvasColor(ColorService.rgbaToInt(backgroundColor))

        // 3. Create layerObjects
        val createPolylineCommand = CreatePolylineCommand(svgObject.polyline)
        createPolylineCommand.execute()

        val createRectangleCommand = CreateRectangleCommand(svgObject.rect)
        createRectangleCommand.execute()

        val createEllipseCommand = CreateEllipseCommand(svgObject.ellipse)
        createEllipseCommand.execute()
    }

    // converts drawables to svg/xml and then converts to base64
    fun getDrawingDataURI(): String {
        val svgObject = DrawingJsonService.getSvgObject()!!

        // svg attribute
        val rootSVG = SVGBuilder("svg")
        rootSVG.addAttr("width", svgObject.width)
        rootSVG.addAttr("height", svgObject.height)
        rootSVG.addAttr("style", svgObject.style)

        val polylineArrayBuilder = U.arrayBuilder()
        svgObject.polyline?.forEach { pencil ->
            if (pencil.id == null || pencil.id == "") return@forEach
            polylineArrayBuilder.add(U.objectBuilder()
                .add("-id", pencil.id)
                .add("-name", "pencil")
                .add("-points", pencil.points)
                .add("-style", pencil.style))
        }
        rootSVG.addArrayAttr("polyline", polylineArrayBuilder)

        return ImageConvertor.XMLToBase64(rootSVG.getXML())
    }
}