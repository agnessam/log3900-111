package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Matrix
import android.graphics.Path
import android.graphics.RectF
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import kotlin.math.abs

class ResizeCommand(objectId: String) : ICommand {
    private var xScale: Float = 1f
    private var yScale: Float = 1f
    private var xTranslate: Float = 0f
    private var yTranslate: Float = 0f
    private var commandToResize: ICommand? = null

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
//        TODO("Not yet implemented")
        if(commandToResize == null) return
        when(commandToResize){
            is PencilCommand -> (commandToResize as PencilCommand).resize(this.xScale, this.yScale, this.xTranslate, this.yTranslate)
            is RectangleCommand -> (commandToResize as RectangleCommand).resize(this.xScale, this.yScale, this.xTranslate, this.yTranslate)
            is EllipseCommand -> (commandToResize as EllipseCommand).resize(this.xScale, this.yScale, this.xTranslate, this.yTranslate)
            else -> {}
        }
    }

    override fun update(drawingCommand: Any) {
//        TODO("Not yet implemented")
    }

    private fun scaleAroundPoint(xScale: Float, yScale: Float, xAnchor: Float, yAnchor: Float, path: Path){
        // math is fun!
        var bounds = RectF()
        path.computeBounds(bounds, true)
        if(xScale < 0){
            var xFlipMatrix = Matrix()
            xFlipMatrix.postScale(-1f, 1f, xAnchor, bounds.centerX())
            path.transform(xFlipMatrix)
        }
        if(yScale < 0){
            var yFlipMatrix = Matrix()
            yFlipMatrix.postScale(1f, -1f, bounds.centerX(), yAnchor)
            path.transform(yFlipMatrix)
        }

        var matrix = Matrix()
        matrix.preTranslate(-xAnchor, -yAnchor)
        path.transform(matrix)

        matrix = Matrix()
        matrix.setScale(abs(xScale), abs(yScale))
        path.transform(matrix)

        matrix = Matrix()
        matrix.postTranslate(xAnchor, yAnchor)

        path.transform(matrix)
    }

    private fun PencilCommand.resize(xScale: Float, yScale: Float, xTranslate: Float, yTranslate: Float) {
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, path)
        execute()
    }

    private fun EllipseCommand.resize(xScale: Float, yScale: Float, xTranslate: Float, yTranslate: Float) {
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, borderPath)
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, fillPath)
        execute()
    }

    private fun RectangleCommand.resize(xScale: Float, yScale: Float, xTranslate: Float, yTranslate: Float){
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, borderPath)
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, fillPath)
        execute()
    }


}