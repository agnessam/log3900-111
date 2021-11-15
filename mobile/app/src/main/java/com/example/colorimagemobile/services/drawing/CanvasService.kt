package com.example.colorimagemobile.services.drawing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import com.example.colorimagemobile.utils.Constants

object CanvasService {
    lateinit var extraBitmap: Bitmap
    lateinit var extraCanvas: Canvas
    var drawableList: Array<Drawable> = arrayOf<Drawable>()
    var layerDrawable: LayerDrawable = LayerDrawable(drawableList)
    private var uuidToLayerIdDict: HashMap<String, Int> = HashMap()

    private var width = Constants.DRAWING.MAX_WIDTH
    private var height = Constants.DRAWING.MAX_HEIGHT

    fun getWidth(): Int {
        return width
    }

    fun getHeight(): Int {
        return height
    }

    fun setWidth(newWidth: Int) {
        width = newWidth
    }

    fun setHeight(newHeight: Int) {
        height = newHeight
    }

    fun updateCanvasColor(color: Int) {
        extraCanvas.drawColor(color)
    }

    // initialize and create new canvas/bitmap
    fun createNewBitmap() {
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
    }

    fun getDrawableFromUuid(uuid: String): Drawable?{
        var layerId = uuidToLayerIdDict[uuid]
        if (layerId != null){
            return layerDrawable.getDrawable(layerId)
        }
        return null
    }

    fun addNewDrawableToDrawing(uuid: String, layerId: Int){
        uuidToLayerIdDict[uuid] = layerId
    }

    // pass bitmap retrieved/calculated from Server data
    fun createExistingBitmap(newBitmap: Bitmap) {
        extraBitmap = newBitmap
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawBitmap(extraBitmap, 0f, 0f, null)
    }
}