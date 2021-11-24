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

    private var resizeFillPath: Path? = null
    private var resizeBorderPath: Path? = null

    var id: String = ""
        get() = when(commandToResize){
            is PencilCommand -> (commandToResize!! as PencilCommand).pencil.id
            is RectangleCommand -> (commandToResize!! as RectangleCommand).rectangle.id
            is EllipseCommand -> (commandToResize as EllipseCommand).ellipse.id
            else -> ""
        }

    init{
        commandToResize = DrawingObjectManager.getCommand(objectId)
        when(commandToResize) {
            is PencilCommand -> (commandToResize as PencilCommand).getPath()
            is EllipseCommand -> (commandToResize as EllipseCommand).getPaths()
            is RectangleCommand -> (commandToResize as RectangleCommand).getPaths()
            else -> null
        }
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
        printMsg("xScale: $xScale yScale: $yScale xTranslate: $xTranslate yTranslate: $yTranslate")
    }

    override fun update(drawingCommand: Any) {
        setScales((drawingCommand as ResizeData).xScaled, drawingCommand.yScaled, drawingCommand.xTranslate, drawingCommand.yTranslate)
        // do something with previous transformations?
    }

    private fun scaleAroundPoint(xScale: Float, yScale: Float, xAnchor: Float, yAnchor: Float, path: Path, isFill: Boolean){
        // math is fun!
        var bounds = RectF()
        path.computeBounds(bounds, true)

        var matrix = Matrix()
        matrix.preTranslate(-xAnchor, -yAnchor)
        path.transform(matrix)

        matrix = Matrix()
        matrix.setScale(xScale, yScale)
        path.transform(matrix)

        matrix = Matrix()
        matrix.postTranslate(xAnchor, yAnchor)

        path.transform(matrix)

        if(isFill){
            lastXScale = xScale
            lastYScale = yScale
        }
    }

    fun getPathBounds(): RectF {
        return when(commandToResize){
            is PencilCommand -> (commandToResize as PencilCommand).getPathBounds()
            is EllipseCommand -> (commandToResize as EllipseCommand).getPathBounds()
            is RectangleCommand -> (commandToResize as RectangleCommand).getPathBounds()
            else -> RectF()
        }
    }

    fun getOriginalPathBounds(): RectF{
        var bounds = RectF()
        resizeBorderPath?.computeBounds(bounds, true)
        return bounds
    }

    private fun PencilCommand.getPathBounds(): RectF{
        var bounds = RectF()
        path.computeBounds(bounds, true)
        return bounds
    }

    private fun EllipseCommand.getPathBounds(): RectF{
        var bounds = RectF()
        borderPath.computeBounds(bounds, true)
        return bounds
    }

    private fun RectangleCommand.getPathBounds(): RectF{
        var bounds = RectF()
        borderPath.computeBounds(bounds, true)
        return bounds
    }

    private fun PencilCommand.getPath(){
        resizeBorderPath = path
    }

    private fun EllipseCommand.getPaths(){
        resizeFillPath = fillPath
        resizeBorderPath = borderPath
    }

    private fun RectangleCommand.getPaths(){
        resizeFillPath = fillPath
        resizeBorderPath = borderPath
    }

    private fun PencilCommand.resize(xScale: Float, yScale: Float, xTranslate: Float, yTranslate: Float) {
        path = Path(resizeBorderPath!!)
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, path, true)
        execute()
    }

    private fun EllipseCommand.resize(xScale: Float, yScale: Float, xTranslate: Float, yTranslate: Float) {
        fillPath = Path(resizeFillPath!!)
        borderPath = Path(resizeBorderPath!!)
        // Fill scaling must always be before fill due to the inverse scale condition
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, fillPath, false)
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, borderPath, true)
        execute()
    }

    private fun RectangleCommand.resize(xScale: Float, yScale: Float, xTranslate: Float, yTranslate: Float){
        fillPath = Path(resizeFillPath!!)
        borderPath = Path(resizeBorderPath!!)
        // Fill scaling must always be before fill due to the inverse scale condition
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, fillPath, false)
        scaleAroundPoint(xScale, yScale, xTranslate, yTranslate, borderPath, true)
        execute()
    }


}