package com.example.colorimagemobile.Interface

import com.example.colorimagemobile.Shape.DrawingPreview

interface BrushViewChangeListener {
    fun onViewAdd(drawingPreview: DrawingPreview?)
    fun onViewRemoved(drawingPreview: DrawingPreview?)
    fun onStartDrawing()
    fun onStopDrawing()
}