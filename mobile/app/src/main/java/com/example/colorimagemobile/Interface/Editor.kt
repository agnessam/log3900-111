package com.example.colorimagemobile.Interface

import android.content.Context
import android.view.View
import androidx.annotation.IntRange
import com.example.colorimagemobile.classes.Shape.*
import com.example.colorimagemobile.classes.drawingEditor.EditorViewClass
import com.example.colorimagemobile.classes.drawingEditor.EditorImpl


interface Editor {

    fun setBrushDrawingMode(brushDrawingMode: Boolean)

    fun setOpacity(@IntRange(from = 0, to = 100) opacity: Int)

    class Builder(var context: Context, editorViewClass: EditorViewClass) {
        var parentView: EditorViewClass = editorViewClass
        var deleteView: View? = null
        var drawingPreview: DrawingPreview = editorViewClass.getDrawingView()!!


        fun build(): Editor {
            return EditorImpl(this)
        }

    }

    /**
     * Update the current shape to be drawn,
     * through the use of a ShapeBuilder.
     */
    fun setShape(shapeBuilder: ShapeBuilder?)
}