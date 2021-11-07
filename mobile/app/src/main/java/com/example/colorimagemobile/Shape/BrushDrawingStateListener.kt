package com.example.colorimagemobile.Shape

import android.view.View
import com.example.colorimagemobile.Interface.BrushViewChangeListener

class BrushDrawingStateListener internal constructor(
    private val mEditorView: EditorView,
    private val mViewState: EditorViewState
) : BrushViewChangeListener {


    override fun onViewAdd(drawingView: DrawingView?) {
        mViewState.addAddedView(drawingView!!)

    }

    override fun onViewRemoved(drawingView: DrawingView?) {
        if (mViewState.addedViewsCount > 0) {
            val removeView: View = mViewState.removeAddedView(
                mViewState.addedViewsCount - 1
            )
            if (removeView !is DrawingView) {
                mEditorView.removeView(removeView)
            }
        }

    }

    override fun onStartDrawing() {

    }

    override fun onStopDrawing() {

    }
}