package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.PencilData
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.*
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService

class PencilCommand(pencilData: PencilData): ICommand {
    var path: Path = Path()
    var pencil: PencilData = pencilData
    private var layerIndex: Int = -1
    private var boundingRectangle = Rect(0,0, CanvasService.extraCanvas.width, CanvasService.extraCanvas.height)
    private var paint: Paint = Paint()

    init{
        val pathShape = PathShape(Path(),
            CanvasService.extraCanvas.width.toFloat(), CanvasService.extraCanvas.height.toFloat())

        var shapeDrawable = ShapeDrawable(pathShape)
        layerIndex = CanvasService.layerDrawable.addLayer(shapeDrawable)
        PencilService.paths.putIfAbsent(layerIndex, path)

        paint.color = ColorService.rgbaToInt(this.pencil.stroke)
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = this.pencil.strokeWidth.toFloat()
        this.setStartingPoint()
    }
    private fun setStartingPoint(){
        this.path.moveTo(pencil.pointsList[0].x, pencil.pointsList[0].y)
    }

    fun addPoint(x: Float, y: Float) {
        val point = Point(x, y)
        pencil.pointsList.add(point)
        this.path.lineTo(point.x, point.y)
    }

    // for synchro
    override fun update(drawingCommand: Any) {
        if(drawingCommand is SyncUpdate){
            this.addPoint(drawingCommand.point.x, drawingCommand.point.y)
        }
    }

    private fun getPathDrawable(): ShapeDrawable {
        return CanvasService.layerDrawable.getDrawable(this.layerIndex) as ShapeDrawable
    }

    // update canvas
    override fun execute() {
//        CanvasService.extraCanvas.drawPath(pencilPaintPath.path, pencilPaintPath.brush.getPaint())
        path = generateFillPath()
//        path.fillType = Path.FillType.EVEN_ODD
        paint = generatePaint()
        val pathShape = PathShape(path,
            CanvasService.extraCanvas.width.toFloat(), CanvasService.extraCanvas.height.toFloat()
        )

        var shapeDrawable = ShapeDrawable(pathShape)
        this.getPathDrawable().bounds = this.boundingRectangle
        CanvasService.layerDrawable.setDrawable(layerIndex, shapeDrawable)
        PencilService.paths[layerIndex] = path

        this.getPathDrawable().paint.set(this.paint)
        CanvasUpdateService.invalidate()
    }

    private fun generatePaint(): Paint{
        var paint = Paint()
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL
        paint.isDither = true
        paint.isAntiAlias = true
        return paint
    }

    private fun generateFillPath(): Path {
        var left = 10f
        var top = 10f
        var right = 200f
        var bottom = 200f
        var rect = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

        val borderPath = Path()

        borderPath.addRect(rect, Path.Direction.CW)
        borderPath.close()

        val innerRect = RectF(rect)
        innerRect.inset(34f, 34f)
        if (innerRect.width() > 0 && innerRect.height() > 0) {
            borderPath.addRect(innerRect, Path.Direction.CW)
            borderPath.close()
        }

        borderPath.setFillType(Path.FillType.EVEN_ODD)
        return borderPath
    }
}