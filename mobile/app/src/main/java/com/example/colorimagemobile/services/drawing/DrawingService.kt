package com.example.colorimagemobile.services.drawing

object DrawingService {

    private var currentDrawingID: String? = null

    fun setCurrentDrawingID(drawingId: String?) {
        currentDrawingID = drawingId
    }

    fun getCurrentDrawingID(): String? {
        return currentDrawingID
    }
}