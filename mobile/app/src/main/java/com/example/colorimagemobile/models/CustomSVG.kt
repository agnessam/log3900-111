package com.example.colorimagemobile.models

data class SvgStyle (
    var fill: String = "",
    var fillOpacity: String = "",
    val fillWidth: Int = 0,
    var stroke: String = "",
    var strokeOpacity: String = "",
    var strokeWidth: Int = 0,
)

data class Polyline (
    val id: String,
    val name: String,
    val points: String,
    val style: String
)

data class Ellipse (
    val id: String,
    val name: String,
    val cx: String,
    val cy: String,
    val width: String,
    val height: String,
    val rx: String,
    val ry: String,
    val style: String,
)

data class BaseSVG(
    val width: String,
    val height: String,
    val style: String,
)

data class CustomSVG(
    val width: String,
    val height: String,
    val style: String,
    val ellipse: ArrayList<Ellipse>?,
    val polyline: ArrayList<Polyline>?
)


