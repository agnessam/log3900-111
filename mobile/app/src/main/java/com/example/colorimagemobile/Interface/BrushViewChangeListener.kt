package com.example.colorimagemobile.Interface

import com.example.colorimagemobile.Shape.DrawingView

interface BrushViewChangeListener {
    fun onViewAdd(drawingView: DrawingView?)
    fun onViewRemoved(drawingView: DrawingView?)
    fun onStartDrawing()
    fun onStopDrawing()
}