package com.example.colorimagemobile.services.drawing

import com.example.colorimagemobile.classes.xml_json.StringParser
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

object DrawingJsonService {

    private var currentSVGObject: CustomSVG? = null

    fun setSVGObject(svg: CustomSVG) {
        currentSVGObject = svg

        if (currentSVGObject?.polyline.isNullOrEmpty()) currentSVGObject?.polyline = arrayListOf()
        if (currentSVGObject?.rect.isNullOrEmpty()) currentSVGObject?.rect = arrayListOf()
        if (currentSVGObject?.ellipse.isNullOrEmpty()) currentSVGObject?.ellipse = arrayListOf()
    }

    fun getSvgObject(): CustomSVG? {
        return currentSVGObject
    }

    fun createPolyline(pencilData: PencilData) {
        val style = StringParser.buildStyle(pencilData as ToolData) + "stroke-linecap: round; stroke-linejoin: round;"
        val initialPoints = "${pencilData.pointsList[0].x} ${pencilData.pointsList[0].y}"

        val polyline = Polyline(id=pencilData.id, name="pencil", points=initialPoints, style=style)
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

        rectangle.width = "${rectangleData.width}px"
        rectangle.height = "${rectangleData.height}px"
        rectangle.x = "${rectangleData.x}px"
        rectangle.y = "${rectangleData.y}px"
    }

    fun createEllipse(ellipseData: EllipseData) {
        val style = StringParser.buildStyle(ellipseData as ToolData)

        val ellipse = Ellipse(
            id=ellipseData.id,
            name="ellipse",
            rx="${ellipseData.width/2}px",
            ry="${ellipseData.height/2}px",
            width="${ellipseData.width}px",
            height="${ellipseData.height}px",
            cx="0px",
            cy="0px",
            style=style
        )
        currentSVGObject?.ellipse?.add(ellipse)
    }

    fun updateEllipse(ellipseData: EllipseData) {
        val ellipse = currentSVGObject?.ellipse?.find { ellipse -> ellipse.id == ellipseData.id } ?: return

        ellipse.width = "${ellipseData.width}px"
        ellipse.height = "${ellipseData.height}px"
        ellipse.rx = "${(ellipseData.width / 2)}px"
        ellipse.ry = "${(ellipseData.height / 2)}px"
        ellipse.cx = "${ellipseData.x}px"
        ellipse.cy = "${ellipseData.y}px"
    }
}