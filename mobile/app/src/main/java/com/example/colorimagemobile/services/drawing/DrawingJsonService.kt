package com.example.colorimagemobile.services.drawing

import com.example.colorimagemobile.classes.xml_json.StringParser
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService

object DrawingJsonService {

    private var currentSVGObject: CustomSVG? = null

    fun setSVGObject(svg: CustomSVG) {
        currentSVGObject = svg

        if (currentSVGObject?.polyline.isNullOrEmpty()) currentSVGObject?.polyline = HashMap()
        if (currentSVGObject?.rect.isNullOrEmpty()) currentSVGObject?.rect = HashMap()
        if (currentSVGObject?.ellipse.isNullOrEmpty()) currentSVGObject?.ellipse = HashMap()
    }

    fun getSvgObject(): CustomSVG? {
        return currentSVGObject
    }

    private fun getPolylineExtraStyle(): String = " stroke-linecap: round; stroke-linejoin: round;"

    fun createPolyline(pencilData: PencilData) {
        val style = StringParser.buildStyle(pencilData as ToolData) + getPolylineExtraStyle()
        val initialPoints = "${pencilData.pointsList[0].x} ${pencilData.pointsList[0].y}"

        val polyline = Polyline(id=pencilData.id, name="pencil", points=initialPoints, style=style)
        currentSVGObject?.polyline?.set(pencilData.id, polyline)
    }

    fun addPointToPolyline(id: String, point: Point) {
        val polyline = currentSVGObject?.polyline?.get(id)
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
        currentSVGObject?.rect?.set(rectangleData.id, rect)
    }

    fun updateRect(rectangleData: RectangleData) {
        val rectangle = currentSVGObject?.rect?.get(rectangleData.id) ?: return

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
        currentSVGObject?.ellipse?.set(ellipseData.id, ellipse)
    }

    fun updateEllipse(ellipseData: EllipseData) {
        val ellipse = currentSVGObject?.ellipse?.get(ellipseData.id)?: return

        ellipse.width = "${ellipseData.width}px"
        ellipse.height = "${ellipseData.height}px"
        ellipse.rx = "${(ellipseData.width / 2)}px"
        ellipse.ry = "${(ellipseData.height / 2)}px"
        ellipse.cx = "${ellipseData.x}px"
        ellipse.cy = "${ellipseData.y}px"
    }

    fun removePolyline(id: String) {
        currentSVGObject?.polyline?.remove(id)
    }

    fun removeRectangle(id: String) {
        currentSVGObject?.rect?.remove(id)
    }

    fun removeEllipse(id: String) {
        currentSVGObject?.ellipse?.remove(id)
    }

    fun updatePolylineWidth(pencilData: PencilData) {
        val id = DrawingObjectManager.getUuid(SelectionService.selectedShapeIndex)

        val polyline = currentSVGObject?.polyline?.get(id)
        polyline?.style = StringParser.buildStyle(pencilData as ToolData) + getPolylineExtraStyle()
    }

    fun updateRectangleWidth(rectangleData: RectangleData) {
        val id = DrawingObjectManager.getUuid(SelectionService.selectedShapeIndex)

        val rect = currentSVGObject?.rect?.get(id)
        rect?.style = StringParser.buildStyle(rectangleData as ToolData)
    }

    fun updateEllipseWidth(ellipseData: EllipseData) {
        val id = DrawingObjectManager.getUuid(SelectionService.selectedShapeIndex)

        val ellipse = currentSVGObject?.ellipse?.get(id)
        ellipse?.style = StringParser.buildStyle(ellipseData as ToolData)
    }

    fun updateShapeTransform(transformString: String, shapeId: String, shapeLabel: ShapeLabel){
        when(shapeLabel){
            ShapeLabel.POLYLINE -> {
                val polyline = currentSVGObject?.polyline?.get(shapeId)
                polyline?.transform = transformString
            }
            ShapeLabel.ELLIPSE -> {
                val ellipse = currentSVGObject?.ellipse?.get(shapeId)
                ellipse?.transform = transformString
            }
            ShapeLabel.RECTANGLE -> {
                val rect = currentSVGObject?.rect?.get(shapeId)
                rect?.transform = transformString
            }
        }
    }
}

enum class ShapeLabel {
    POLYLINE,
    RECTANGLE,
    ELLIPSE
}