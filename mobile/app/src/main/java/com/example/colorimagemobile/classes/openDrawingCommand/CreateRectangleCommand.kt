package com.example.colorimagemobile.classes.openDrawingCommand

import com.example.colorimagemobile.classes.CommandFactory
import com.example.colorimagemobile.classes.toolsCommand.RectangleCommand
import com.example.colorimagemobile.classes.xml_json.StringParser
import com.example.colorimagemobile.models.Rectangle
import com.example.colorimagemobile.models.RectangleData
import com.example.colorimagemobile.models.SvgStyle
import com.example.colorimagemobile.services.drawing.Point

class CreateRectangleCommand(rectangles: HashMap<String, Rectangle>?): ICreateDrawingCommand {

    private val rectangles = rectangles

    override fun createData(style: SvgStyle): RectangleData {
        return RectangleData(
            id = "",
            fill = style.fill,
            stroke = style.stroke,
            fillOpacity = style.fillOpacity,
            strokeOpacity = style.strokeOpacity,
            strokeWidth = style.strokeWidth,
            x = 0f,
            y = 0f,
            width = 0f,
            height = 0f
        )
    }

    override fun execute() {
        if (rectangles?.size == 0) return

        rectangles?.forEach { (id,rectangle) ->
            if (rectangle.id.isNullOrEmpty()) return@forEach

            val style = StringParser.getStyles(rectangle.style)
            val rectangleData = createData(style)

            rectangleData.id = rectangle.id
            rectangleData.x = StringParser.removePX(rectangle.x).toFloat()
            rectangleData.y = StringParser.removePX(rectangle.y).toFloat()
            rectangleData.width = StringParser.removePX(rectangle.width).toFloat()
            rectangleData.height = StringParser.removePX(rectangle.height).toFloat()

            val command = CommandFactory.createCommand("Rectangle", rectangleData) as RectangleCommand
            val endPoint = Point(
                (rectangleData.x + rectangleData.width).toFloat(),
                (rectangleData.y + rectangleData.height).toFloat()
            )

            command.setEndPoint(endPoint)
            command.execute()

            if(rectangle.transform != null) {
                transformShape(rectangle.transform, rectangle.id)
            }
        }
    }
}