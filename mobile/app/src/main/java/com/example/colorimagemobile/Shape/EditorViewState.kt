package com.example.colorimagemobile.Shape

import android.view.View
import java.util.*

class EditorViewState {
    var currentSelectedView: View? = null

    private val addedViews: MutableList<View>
    private val redoViews: Stack<View>

    fun clearCurrentSelectedView() {
        currentSelectedView = null
    }

    fun getAddedView(index: Int): View {
        return addedViews[index]
    }

    val addedViewsCount: Int
        get() = addedViews.size

    fun clearAddedViews() {
        addedViews.clear()
    }

    fun addAddedView(view: View) {
        addedViews.add(view)
    }

    fun removeAddedView(index: Int): View {
        return addedViews.removeAt(index)
    }

//    fun containsAddedView(view: View): Boolean {
//        return addedViews.contains(view)
//    }

    init {
        addedViews = ArrayList()
        redoViews = Stack()
    }
}