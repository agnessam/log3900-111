package com.example.colorimagemobile.services.drawing

import com.example.colorimagemobile.classes.xml_json.StringParser
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

object DrawingJsonService {

    private var currentSVGObject: CustomSVG? = null

    fun setSVGObject(svg: CustomSVG) {
        currentSVGObject = svg
    }

    fun getSvgObject(): CustomSVG? {
        return currentSVGObject
    }

    fun createPolyline(pencilData: PencilData) {
        val style = StringParser.buildStyle(pencilData as ToolData)
        val points = getStringPoints(pencilData.pointsList)

        val polyline = Polyline(id=pencilData.id, name="pencil", points=points, style=style)
        currentSVGObject?.polyline?.add(polyline)
    }

    fun addPointToPolyline(id: String, point: Point) {
        val polyline = currentSVGObject?.polyline?.find { line -> line.id == id }
        polyline?.points += ", ${point.x} ${point.y}"
    }

    fun createRect(rectangleData: RectangleData) {
        val style = StringParser.buildStyle(rectangleData as ToolData)

        val rect = Rectangle(
            id=rectangleData.id,
            name="rectangle",
            x="${rectangleData.x}px",
            y="${rectangleData.y}px",
            width="${rectangleData.width}px",
            height="${rectangleData.height}px",
            style=style
        )
        currentSVGObject?.rect?.add(rect)
    }

    fun updateRect(rectangleData: RectangleData) {
        val rectangle = currentSVGObject?.rect?.find { rect -> rect.id == rectangleData.id } ?: return
        val index = currentSVGObject?.rect?.indexOf(rectangle)

        currentSVGObject?.rect!![index!!].width = "${rectangleData.width}px"
        currentSVGObject?.rect!![index].height = "${rectangleData.height}px"
        currentSVGObject?.rect!![index].x = "${rectangleData.x}px"
        currentSVGObject?.rect!![index].y = "${rectangleData.y}px"
    }

    // TO REMOVE CUZ ONLY ONE POINT
    private fun getStringPoints(points: ArrayList<Point>): String {
        var pointsString = ""
        points.forEach { point ->
            pointsString += "${point.x} ${point.y}"
        }

        pointsString.dropLast(1)

        return pointsString
    }
}