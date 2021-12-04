package com.example.colorimagemobile.models

data class SvgStyle (
    var fill: String = "",
    var fillOpacity: String = "1",
    val fillWidth: Int = 0,
    var stroke: String = "",
    var strokeOpacity: String = "1",
    var strokeWidth: Int = 0,
)

interface Shape {
    val id: String
    val name: String
    var style: String
    var transform: String
}

data class Polyline (
    override val id: String,
    override val name: String,
    override var style: String,
    override var transform: String = "",
    var points: String
): Shape

data class Ellipse (
    override val id: String,
    override val name: String,
    override var style: String,
    override var transform: String = "",
    var cx: String,
    var cy: String,
    var width: String,
    var height: String,
    var rx: String,
    var ry: String
): Shape

data class Rectangle (
    override val id: String,
    override val name: String,
    override var style: String,
    override var transform: String = "",
    var x: String,
    var y: String,
    var width: String,
    var height: String,
): Shape

data class BaseSVG(
    val width: String,
    val height: String,
    val style: String,
)

data class CustomSVG(
    val width: String,
    val height: String,
    val style: String,
    var shapes: ArrayList<Shape>?,
)


