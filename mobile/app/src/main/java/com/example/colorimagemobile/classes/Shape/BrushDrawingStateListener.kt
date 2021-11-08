package com.example.colorimagemobile.classes.Shape

import android.view.View
import com.example.colorimagemobile.Interface.BrushViewChangeListener
import com.example.colorimagemobile.classes.drawingEditor.EditorViewClass
import com.example.colorimagemobile.classes.drawingEditor.EditorViewState

class BrushDrawingStateListener internal constructor(
    private val mEditorViewClass: EditorViewClass,
    private val mViewState: EditorViewState
) : BrushViewChangeListener {


    override fun onViewAdd(drawingPreview: DrawingPreview?) {
        mViewState.addAddedView(drawingPreview!!)

    }

    override fun onViewRemoved(drawingPreview: DrawingPreview?) {
        if (mViewState.addedViewsCount > 0) {
            val removeView: View = mViewState.removeAddedView(
                mViewState.addedViewsCount - 1
            )
            if (removeView !is DrawingPreview) {
                mEditorViewClass.removeView(removeView)
            }
        }

    }

    override fun onStartDrawing() {

    }

    override fun onStopDrawing() {

    }
}