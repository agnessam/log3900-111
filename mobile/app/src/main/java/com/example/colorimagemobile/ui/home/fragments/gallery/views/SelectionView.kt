package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import com.example.colorimagemobile.classes.toolsCommand.SelectionCommand
import com.example.colorimagemobile.services.drawing.CanvasService
import kotlin.io.path.Path

class SelectionView(context: Context?): CanvasView(context) {
    private var selectionCommand: SelectionCommand? = null

    override fun createPathObject() {
        var selectionRectangle = ShapeDrawable(RectShape())

    }

    fun getPathBoundingBox(path: Path): Region {
        val rectF = RectF()
        path.computeBounds(rectF, true)
        var region = Region()
        region.setPath(
            path,
            Region(
                rectF.left.toInt(), rectF.top.toInt(), rectF.right.toInt(), rectF.bottom.toInt()
            )
        )
        return region
    }

    override fun onTouchDown() {
        val numberOfLayers = CanvasService.layerDrawable.numberOfLayers
        for (i in numberOfLayers - 1 downTo 0) {
            val drawable = CanvasService.layerDrawable.findDrawableByLayerId(i)

        }

    }

    override fun onTouchMove() {
        TODO("Not yet implemented")
    }

    override fun onTouchUp() {
        TODO("Not yet implemented")
    }
}