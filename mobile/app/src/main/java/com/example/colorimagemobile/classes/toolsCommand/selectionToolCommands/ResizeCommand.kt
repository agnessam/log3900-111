package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Matrix
import android.graphics.Path
import android.graphics.RectF
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.ResizeData
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import kotlin.math.abs
import kotlin.math.max

class ResizeCommand(objectId: String) : ICommand {
    private var xScale: Float = 1f
    private var yScale: Float = 1f
    private var lastXScale: Float = 1f
    private var lastYScale: Float = 1f
    private var xTranslate: Float = 0f
    private var yTranslate: Float = 0f
    private var commandToResize: ICommand? = null

    var id: String = ""
        get() = when(commandToResize){
            is PencilCommand -> (commandToResize!! as PencilCommand).pencil.id
            is RectangleCommand -> (commandToResize!! as RectangleCommand).rectangle.id
            is EllipseCommand -> (commandToResize as EllipseCommand).ellipse.id
            else -> ""
        }

    init{
        commandToResize = DrawingObjectManager.getCommand(objectId)
    }

    fun setScales(xScale: Float, yScale: Float, xTranslate: Float, yTranslate: Float){
        this.xScale = xScale
        this.yScale = yScale
        this.xTranslate = xTranslate
        this.yTranslate = yTranslate
    }

    override fun execute() {
        if(commandToResize == null) return
        when(commandToResize){
            is PencilCommand -> (commandToResize as PencilCommand).resize(this.xScale, this.yScale, this.xTranslate, this.yTranslate)
            is RectangleCommand -> (commandToResize as RectangleCommand).resize(this.xScale, this.yScale, this.xTranslate, this.yTranslate)
            is EllipseCommand -> (commandToResize as EllipseCommand).resize(this.xScale, this.yScale, this.xTranslate, this.yTranslate)
            else -> {}
        }
    }

    override fun update(drawingCommand: Any) {
        setScales((drawingCommand as ResizeData).xScaled, drawingCommand.yScaled, drawingCommand.xTranslate, drawingCommand.yTranslate)
        // do something with previous transformations?
    }

    private fun scaleAroundPoint(xScale: Float, yScale: Float, xAnchor: Float, yAnchor: Float, path: Path, isFill: Boolean){
        // math is fun!
        var bounds = RectF()
        path.computeBounds(bounds, true)

        if(xScale == 0f || yScale == 0f) {
            return
        }

        var matrix = Matrix()
        matrix.preTranslate(-xAnchor, -yAnchor)
        path.transform(matrix)

        if((xScale > 0 && lastXScale < 0) || (xScale < 0 && lastXScale > 0)){
            var xFlipMatrix = Matrix()
            xFlipMatrix.setScale(-1f, 1f)
            path.transform(xFlipMatrix)
        }
        if((yScale > 0 && lastYScale < 0) || (yScale < 0 && lastYScale > 0)){
            var yFlipMatrix = Matrix()
            yFlipMatrix.setScale(1f, -1f)
            path.transform(yFlipMatrix)
        }

        matrix = Matrix()
        matrix.setScale(abs(xScale), abs(yScale))
        path.transform(matrix)

        matrix = Matrix()
        matrix.postTranslate(xAnchor, yAnchor)

        path.transform(matrix)

        if(isFill){
            lastXScale = xScale
            lastYScale = yScale
        }
    }

    private fun PencilCommand.resize(xScale: Float, yScale: Float, xTranslate: Float, yTranslate: Float) {
        var tempPath = path
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, path, true)
        execute()
        path = tempPath
    }

    private fun EllipseCommand.resize(xScale: Float, yScale: Float, xTranslate: Float, yTranslate: Float) {
        var tempBorderPath = borderPath
        var tempFillPath = fillPath
        // Fill scaling must always be before fill due to the inverse scale condition
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, fillPath, false)
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, borderPath, true)
        execute()
        borderPath = tempBorderPath
        fillPath = tempFillPath
    }

    private fun RectangleCommand.resize(xScale: Float, yScale: Float, xTranslate: Float, yTranslate: Float){
        var tempBorderPath = borderPath
        var tempFillPath = fillPath
        // Fill scaling must always be before fill due to the inverse scale condition
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, fillPath, false)
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, borderPath, true)
        execute()
        borderPath = tempBorderPath
        fillPath = tempFillPath
    }


}