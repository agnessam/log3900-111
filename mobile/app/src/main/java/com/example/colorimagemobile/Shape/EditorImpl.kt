package com.example.colorimagemobile.Shape


import android.content.Context
import android.view.View
import androidx.annotation.IntRange
import com.example.colorimagemobile.Interface.Editor


class EditorImpl(builder: Editor.Builder): Editor {

    private var parentView: EditorView? = null
    private var viewState: EditorViewState? = null
    private var deleteView: View? = null
    private var drawingPreview: DrawingPreview? = null
    private var mBrushDrawingStateListener: BrushDrawingStateListener? = null
    private var context: Context? = null

     init {
        context = builder.context
        parentView = builder.parentView
        deleteView = builder.deleteView
        drawingPreview = builder.drawingPreview
        viewState = EditorViewState()
        mBrushDrawingStateListener = BrushDrawingStateListener(builder.parentView, viewState!!)
        drawingPreview!!.setBrushViewChangeListener(mBrushDrawingStateListener!!)

    }

    override fun setBrushDrawingMode(brushDrawingMode: Boolean) {
        if (drawingPreview != null) {
            drawingPreview!!.enableDrawing(brushDrawingMode)
        }
    }

    override fun setOpacity(@IntRange(from = 0, to = 100) opacity: Int) {
        var opacity = opacity
        if (drawingPreview != null && drawingPreview!!.currentShapeBuilder != null) {
            opacity = (opacity / 100.0 * 255.0).toInt()
            drawingPreview!!.currentShapeBuilder!!.withShapeOpacity(opacity)
        }
    }


    override fun setShape(shapeBuilder: ShapeBuilder?) {
        drawingPreview!!.setShapeBuilder(shapeBuilder!!)
    }

}