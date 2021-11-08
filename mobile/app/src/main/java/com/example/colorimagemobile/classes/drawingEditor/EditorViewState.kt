package com.example.colorimagemobile.classes.drawingEditor

import android.view.View
import java.util.*

class EditorViewState {

    private val addedViews: MutableList<View>
    private val redoViews: Stack<View>

    val addedViewsCount: Int
        get() = addedViews.size


    fun addAddedView(view: View) {
        addedViews.add(view)
    }

    fun removeAddedView(index: Int): View {
        return addedViews.removeAt(index)
    }

    init {
        addedViews = ArrayList()
        redoViews = Stack()
    }
}