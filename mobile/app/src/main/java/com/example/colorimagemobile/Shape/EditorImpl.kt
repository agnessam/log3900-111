package com.example.colorimagemobile.Shape


import android.content.Context
import android.view.View
import androidx.annotation.IntRange
import com.example.colorimagemobile.Interface.Editor


class EditorImpl(builder: Editor.Builder): Editor {

    private var parentView: EditorView? = null
    private var viewState: EditorViewState? = null
    private var deleteView: View? = null
    private var drawingView: DrawingView? = null
    private var mBrushDrawingStateListener: BrushDrawingStateListener? = null
    private var context: Context? = null

     init {
        context = builder.context
        parentView = builder.parentView
        deleteView = builder.deleteView
        drawingView = builder.drawingView
        viewState = EditorViewState()
        mBrushDrawingStateListener = BrushDrawingStateListener(builder.parentView, viewState!!)
        drawingView!!.setBrushViewChangeListener(mBrushDrawingStateListener!!)

    }

    override fun setBrushDrawingMode(brushDrawingMode: Boolean) {
        if (drawingView != null) {
            drawingView!!.enableDrawing(brushDrawingMode)
        }
    }

    override fun setOpacity(@IntRange(from = 0, to = 100) opacity: Int) {
        var opacity = opacity
        if (drawingView != null && drawingView!!.currentShapeBuilder != null) {
            opacity = (opacity / 100.0 * 255.0).toInt()
            drawingView!!.currentShapeBuilder!!.withShapeOpacity(opacity)
        }
    }

    override fun setBrushEraserSize(brushEraserSize: Float) {
        if (drawingView != null) {
            drawingView!!.setBrushEraserSize(brushEraserSize)
        }
    }


    override fun brushEraser() {
        if (drawingView != null) drawingView!!.brushEraser()

    }

    override fun setShape(shapeBuilder: ShapeBuilder?) {
        drawingView!!.setShapeBuilder(shapeBuilder!!)
    }

}