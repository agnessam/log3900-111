package com.example.colorimagemobile.services.drawing

import com.example.colorimagemobile.classes.xml_json.StringParser
import com.example.colorimagemobile.models.CustomSVG
import com.example.colorimagemobile.models.PencilData
import com.example.colorimagemobile.models.Polyline
import com.example.colorimagemobile.models.ToolData

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

    private fun getStringPoints(points: ArrayList<Point>): String {
        var pointsString = ""
        points.forEach { point ->
            pointsString += "${point.x} ${point.y}"
        }

        pointsString.dropLast(1)

        return pointsString
    }
}