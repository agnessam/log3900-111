package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.PencilData
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.*
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import android.graphics.RectF
import android.graphics.drawable.ScaleDrawable
import android.view.Gravity
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

        // Add layerIndex with id to CanvasService
        CanvasService.addNewDrawableToDrawing(pencilData.id, layerIndex)

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

    fun scaleTest(){
//        var quadrant = 3
//        var ratio = 2
//        var currentPathBoundsRectF = RectF()
//        this.path.computeBounds(currentPathBoundsRectF, true)
//        var xTranslation: Int
//        var yTranslation: Int
//        when(quadrant){
//            1 -> {
//                xTranslation = -(currentPathBoundsRectF.left * (ratio - 1)).toInt()
//                yTranslation = -(currentPathBoundsRectF.bottom * (ratio - 1)).toInt()
//            }
//            2 -> {
//                xTranslation = -(currentPathBoundsRectF.right * (ratio - 1)).toInt()
//                yTranslation = -(currentPathBoundsRectF.bottom * (ratio - 1)).toInt()
//            }
//            3-> {
//                xTranslation = -(currentPathBoundsRectF.right * (ratio - 1)).toInt()
//                yTranslation = -(currentPathBoundsRectF.top * (ratio - 1)).toInt()
//            }
//            4 -> {
//                xTranslation = -(currentPathBoundsRectF.left * (ratio - 1)).toInt()
//                yTranslation = -(currentPathBoundsRectF.top * (ratio - 1)).toInt()
//            }
//            else -> {
//                xTranslation = 0
//                yTranslation = 0
//            }
//        }
//
//        this.getPathDrawable().bounds = Rect(0 + xTranslation,
//            0 + yTranslation,
//            CanvasService.extraCanvas.width * ratio + xTranslation,
//            CanvasService.extraCanvas.height * ratio + yTranslation)
    }
}