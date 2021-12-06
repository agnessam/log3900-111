package com.example.colorimagemobile.services.drawing

import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import com.example.colorimagemobile.classes.toolsCommand.EllipseCommand
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.classes.toolsCommand.RectangleCommand

object PreviewBoxManager {
    private var previewBoxDrawables: LayerDrawable = LayerDrawable(arrayOf<Drawable>())
    private var uuidPreviewBoxDrawableMap: HashMap<String, Int> = HashMap()

    fun clearPreviewBoxes() {
        for(i in 0 until previewBoxDrawables.numberOfLayers){
            var emptyShapeDrawable = ShapeDrawable()
            emptyShapeDrawable.paint.alpha = 0
            previewBoxDrawables.setDrawable(i, emptyShapeDrawable)
        }
        previewBoxDrawables = LayerDrawable(arrayOf<Drawable>())
        uuidPreviewBoxDrawableMap = HashMap()
    }

    fun draw(canvas: Canvas){
        previewBoxDrawables.draw(canvas)
    }

    fun modifyPreviewBox(id: String) {
        var previewBoxIndex = uuidPreviewBoxDrawableMap[id]
        if(previewBoxIndex is Int){
            var previewBox = previewBoxDrawables.getDrawable(previewBoxIndex)
            previewBox.bounds = (getShapeBounds(id) ?: return)
        }
        else{
            addPreviewBox(id)
        }
        CanvasUpdateService.invalidate()
    }

    fun removePreviewBox(id: String){
        var emptyShapeDrawable = ShapeDrawable()
        emptyShapeDrawable.paint.alpha = 0
        var index = uuidPreviewBoxDrawableMap[id]
        if(index != null) previewBoxDrawables.setDrawable(index ,emptyShapeDrawable)
        uuidPreviewBoxDrawableMap.remove(id)
        CanvasUpdateService.invalidate()
    }

    private fun getShapeBounds(id: String): Rect? {
        var shapeBounds = RectF()
        var command = DrawingObjectManager.getCommand(id)
        when(command) {
            is PencilCommand -> command.path.computeBounds(shapeBounds, true)
            is RectangleCommand -> command.borderPath.computeBounds(shapeBounds, true)
            is EllipseCommand -> command.borderPath.computeBounds(shapeBounds, true)
            else -> return null
        }
        var rect = Rect()
        shapeBounds.roundOut(rect)
        return rect
    }

    private fun addPreviewBox(id: String){
        var rectDrawable = ShapeDrawable(RectShape())

        rectDrawable.bounds = (getShapeBounds(id) ?: return)

        var paint = Paint()
        paint.color = Color.BLUE
        paint.alpha = 180
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        paint.pathEffect = DashPathEffect(floatArrayOf(10f,10f), 0f)
        rectDrawable.paint.set(paint)

        uuidPreviewBoxDrawableMap[id] = previewBoxDrawables.addLayer(rectDrawable)
    }
}