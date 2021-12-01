package com.example.colorimagemobile.classes.openDrawingCommand

import com.example.colorimagemobile.classes.CommandFactory
import com.example.colorimagemobile.classes.toolsCommand.EllipseCommand
import com.example.colorimagemobile.classes.xml_json.StringParser
import com.example.colorimagemobile.models.Ellipse
import com.example.colorimagemobile.models.EllipseData
import com.example.colorimagemobile.models.SvgStyle
import com.example.colorimagemobile.services.drawing.Point

class CreateEllipseCommand(ellipses: ArrayList<Ellipse>?): ICreateDrawingCommand {

    private val ellipses = ellipses

    override fun createData(style: SvgStyle): EllipseData {
        return EllipseData(
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
        if (ellipses?.size == 0) return

        ellipses?.forEach { ellipse ->
            if (ellipse.id.isNullOrEmpty()) return@forEach

            val style = StringParser.getStyles(ellipse.style)
            val ellipseData = createData(style)

            // add ellipse attributes
            ellipseData.id = ellipse.id
            ellipseData.width = StringParser.removePX(ellipse.width).toFloat()
            ellipseData.height = StringParser.removePX(ellipse.height).toFloat()
            ellipseData.x = StringParser.removePX(ellipse.cx).toFloat() - StringParser.removePX(ellipse.rx).toFloat()
            ellipseData.y = StringParser.removePX(ellipse.cy).toFloat() - StringParser.removePX(ellipse.ry).toFloat()

            // create ending point and execute command to draw
            val command = CommandFactory.createCommand("Ellipse", ellipseData) as EllipseCommand
            val endPoint = Point(
                (StringParser.removePX(ellipse.cx).toFloat() + StringParser.removePX(ellipse.rx).toFloat()),
                (StringParser.removePX(ellipse.cy).toFloat() + StringParser.removePX(ellipse.ry).toFloat()))

            command.setEndPoint(endPoint)
            command.execute()
        }
    }
}