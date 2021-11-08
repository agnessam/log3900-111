package com.example.colorimagemobile.Interface

import android.graphics.Canvas
import android.graphics.Paint

interface Shape {
    fun draw(canvas: Canvas, paint: Paint?)
    fun startShape(x: Float, y: Float)
    fun moveShape(x: Float, y: Float)
}