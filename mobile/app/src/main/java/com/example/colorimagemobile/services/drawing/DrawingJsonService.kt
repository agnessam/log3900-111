package com.example.colorimagemobile.services.drawing

import com.example.colorimagemobile.classes.xml_json.StringParser
import com.example.colorimagemobile.models.*
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService

object DrawingJsonService {

    private var currentSVGObject: CustomSVG? = null

    fun setSVGObject(svg: CustomSVG) {
        currentSVGObject = svg

        if (currentSVGObject?.shapes.isNullOrEmpty()) currentSVGObject?.shapes = ArrayList()
    }

    fun getSvgObject(): CustomSVG? {
        return currentSVGObject
    }

    private fun getPolylineExtraStyle(): String = " stroke-linecap: round; stroke-linejoin: round;"

    fun createPolyline(pencilData: PencilData) {
        val style = StringParser.buildStyle(pencilData as ToolData) + getPolylineExtraStyle()
        val initialPoints = "${pencilData.pointsList[0].x} ${pencilData.pointsList[0].y}"

        val polyline = Polyline(id=pencilData.id, name="pencil", points=initialPoints, style=style)
        currentSVGObject?.shapes?.add(polyline)
    }

    fun addPointToPolyline(id: String, point: Point) {
        val polyline = currentSVGObject?.shapes?.find { line -> line.id == id }
        if(polyline is Polyline) {
            polyline.points += ", ${point.x} ${point.y}"
        }
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
        currentSVGObject?.shapes?.add(rect)
    }

    fun updateRect(rectangleData: RectangleData) {
        val rectangle = currentSVGObject?.shapes?.find { rect -> rect.id == rectangleData.id } ?: return
        if(rectangle is Rectangle){
            rectangle.width = "${rectangleData.width}px"
            rectangle.height = "${rectangleData.height}px"
            rectangle.x = "${rectangleData.x}px"
            rectangle.y = "${rectangleData.y}px"
        }
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
        currentSVGObject?.shapes?.add(ellipse)
    }

    fun updateEllipse(ellipseData: EllipseData) {
        val ellipse = currentSVGObject?.shapes?.find { ellipse -> ellipse.id == ellipseData.id } ?: return
        if(ellipse is Ellipse){
            ellipse.width = "${ellipseData.width}px"
            ellipse.height = "${ellipseData.height}px"
            ellipse.rx = "${(ellipseData.width / 2)}px"
            ellipse.ry = "${(ellipseData.height / 2)}px"
            ellipse.cx = "${ellipseData.x}px"
            ellipse.cy = "${ellipseData.y}px"
        }
    }

    fun removePolyline(id: String) {
        currentSVGObject?.shapes = currentSVGObject?.shapes?.filter { shape -> shape.id != id } as ArrayList<Shape>
    }

    fun removeRectangle(id: String) {
        currentSVGObject?.shapes = currentSVGObject?.shapes?.filter { shape -> shape.id != id } as ArrayList<Shape>
    }

    fun removeEllipse(id: String) {
        currentSVGObject?.shapes = currentSVGObject?.shapes?.filter { shape -> shape.id != id } as ArrayList<Shape>
    }

    fun updatePolylineWidth(pencilData: PencilData) {
        val id = DrawingObjectManager.getUuid(SelectionService.selectedShapeIndex)

        val polyline = currentSVGObject?.shapes?.find { polyline -> polyline.id == id }
        polyline?.style = StringParser.buildStyle(pencilData as ToolData) + getPolylineExtraStyle()
    }

    fun updateRectangleWidth(rectangleData: RectangleData) {
        val id = DrawingObjectManager.getUuid(SelectionService.selectedShapeIndex)

        val rect = currentSVGObject?.shapes?.find { rect -> rect.id == id }
        rect?.style = StringParser.buildStyle(rectangleData as ToolData)
    }

    fun updateEllipseWidth(ellipseData: EllipseData) {
        val id = DrawingObjectManager.getUuid(SelectionService.selectedShapeIndex)

        val ellipse = currentSVGObject?.shapes?.find { ellipse -> ellipse.id == id }
        ellipse?.style = StringParser.buildStyle(ellipseData as ToolData)
    }

    fun updateShapeTransform(transformString: String, shapeId: String, shapeLabel: ShapeLabel){
        val shape = currentSVGObject?.shapes?.find { shape -> shape.id == shapeId }
        shape?.transform = transformString
    }

    fun updateShapeStrokeColor(rgbColor: String, opacity: String, shapeId: String, shapeLabel: ShapeLabel){
        val shape = currentSVGObject?.shapes?.find { shape -> shape.id == shapeId }
        shape?.style ?: return
        shape.style = generateNewStrokeColorString(rgbColor, opacity, shape.style)
    }

    fun updateShapeFillColor(rgbColor: String, opacity: String, shapeId: String, shapeLabel: ShapeLabel){
        val shape = currentSVGObject?.shapes?.find { shape -> shape.id == shapeId }
        shape?.style ?: return
        shape.style = generateNewFillColorString(rgbColor, opacity, shape.style)
    }

    private fun generateNewStrokeColorString(rgbColor: String, opacity: String, originalStyle: String): String{
        val styleProperties = originalStyle.split(";").toMutableList()
        var newStyleString = ""
        for(i in styleProperties.indices){
            if(styleProperties[i].contains("stroke:")){
                styleProperties[i] = " stroke: $rgbColor"
            }
            else if(styleProperties[i].contains("stroke-opacity")){
                styleProperties[i] = " stroke-opacity: $opacity"
            }
            newStyleString += styleProperties[i] + ";"
        }
        return newStyleString
    }

    private fun generateNewFillColorString(rgbColor: String, opacity: String, originalStyle: String): String{
        val styleProperties = originalStyle.split(";").toMutableList()
        var newStyleString = ""
        for(i in styleProperties.indices){
            if(styleProperties[i].contains("fill:")){
                styleProperties[i] = " fill: $rgbColor"
            }
            else if(styleProperties[i].contains("fill-opacity")){
                styleProperties[i] = " fill-opacity: $opacity"
            }
            newStyleString += styleProperties[i] + ";"
        }
        return newStyleString
    }
}

enum class ShapeLabel {
    POLYLINE,
    RECTANGLE,
    ELLIPSE
}