package com.example.colorimagemobile.Interface

import android.content.Context
import android.view.View
import androidx.annotation.IntRange
import com.example.colorimagemobile.Shape.*


interface Editor {

    fun setBrushDrawingMode(brushDrawingMode: Boolean)

    fun setOpacity(@IntRange(from = 0, to = 100) opacity: Int)

    fun setBrushEraserSize(brushEraserSize: Float)

    fun brushEraser()

    class Builder(var context: Context, editorView: EditorView) {
        var parentView: EditorView = editorView
        var deleteView: View? = null
        var drawingView: DrawingView = editorView.getDrawingView()!!


        fun build(): Editor {
            return EditorImpl(this)
        }

    }

    /**
     * Update the current shape to be drawn,
     * through the use of a ShapeBuilder.
     */
    fun setShape(shapebuilder: ShapeBuilder?)
}