package com.example.colorimagemobile.services.drawing

import android.graphics.drawable.Drawable

object DrawingService {

    private var currentDrawingID: String? = null

    fun setCurrentDrawingID(drawingId: String?) {
        currentDrawingID = drawingId
    }

    fun getCurrentDrawingID(): String? {
        return currentDrawingID
    }
}