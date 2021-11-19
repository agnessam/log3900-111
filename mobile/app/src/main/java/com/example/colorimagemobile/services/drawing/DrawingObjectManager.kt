package com.example.colorimagemobile.services.drawing

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.interfaces.ICommand

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

    fun getCommand(layerIndex: Int): ICommand? {
        return commandList[layerIdUuidMap[layerIndex]]
    }

    fun clearLayers() {
        layerDrawable = LayerDrawable(arrayOf<Drawable>())
        commandList.clear()
        layerIdUuidMap.clear()
    }
}