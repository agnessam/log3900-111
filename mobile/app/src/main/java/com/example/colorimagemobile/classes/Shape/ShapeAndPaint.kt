package com.example.colorimagemobile.classes.Shape

import android.graphics.Paint

class ShapeAndPaint(val id: Int, var shape: AbstractShape, val paint: Paint, val points: ArrayList<Point>)

data class Point(val x: Float, val y: Float)