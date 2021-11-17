package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.EllipseData
import com.example.colorimagemobile.models.EllipseUpdate
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import java.lang.Math.abs

class EllipseCommand(ellipseData: EllipseData): ICommand {
    private var startingPoint: Point? = null
    private var endingPoint: Point? = null

    private var layerIndex: Int = -1
    private var fillEllipseIndex: Int = -1
    private var borderEllipseIndex: Int = -1

    var ellipse: EllipseData = ellipseData
    private lateinit var ellipseShape: LayerDrawable
    private var borderPaint: Paint = Paint()
    private var fillPaint: Paint = Paint()
    init{
        if(layerIndex == -1){
            var borderEllipse = ShapeDrawable(OvalShape())
            var fillEllipse = ShapeDrawable(OvalShape())
            var ellipseShapeArray = arrayOf<Drawable>()
            ellipseShape = LayerDrawable(ellipseShapeArray)
            fillEllipseIndex = ellipseShape.addLayer(fillEllipse)
            borderEllipseIndex = ellipseShape.addLayer(borderEllipse)
            layerIndex = DrawingObjectManager.addLayer(ellipseShape, ellipse.id)
        }
        borderPaint.color = if(ellipseData.stroke != "none") ColorService.rgbaToInt(ellipseData.stroke)
        else Color.WHITE
        fillPaint.color = if(ellipseData.fill != "none") ColorService.rgbaToInt(ellipseData.fill)
        else Color.BLACK

        borderPaint.style = Paint.Style.STROKE
        fillPaint.style = Paint.Style.FILL

        borderPaint.strokeJoin = Paint.Join.ROUND
        borderPaint.strokeCap = Paint.Cap.ROUND
        borderPaint.strokeWidth = this.ellipse.strokeWidth.toFloat()


        setStartPoint(Point(ellipse.x.toFloat(), ellipse.y.toFloat()))
    }

    private fun setStartPoint(startPoint: Point) {
        startingPoint = startPoint
    }

    fun setEndPoint(endPoint: Point) {
        endingPoint = endPoint
        ellipse.width = kotlin.math.abs(endingPoint!!.x - startingPoint!!.x).toInt()
        ellipse.x = ((endingPoint!!.x + startingPoint!!.x) / 2).toInt()
        ellipse.height = kotlin.math.abs(endingPoint!!.y - startingPoint!!.y).toInt()
        ellipse.y = ((endingPoint!!.y + startingPoint!!.y) / 2).toInt()
    }

    private fun getFillEllipse(): ShapeDrawable{
        return ellipseShape.getDrawable(fillEllipseIndex) as ShapeDrawable
    }

    private fun getBorderEllipse(): ShapeDrawable{
        return ellipseShape.getDrawable(borderEllipseIndex) as ShapeDrawable
    }

    private fun getEllipseDrawable(): LayerDrawable {
        return DrawingObjectManager.getDrawable(this.layerIndex) as LayerDrawable
    }

    override fun update(drawingCommand: Any) {
        if(drawingCommand is EllipseUpdate){
            ellipse.x = drawingCommand.x
            ellipse.y = drawingCommand.y
            ellipse.width = drawingCommand.width
            ellipse.height = drawingCommand.height
        }
    }

    override fun execute() {
        var left = ellipse.x - ellipse.width / 2
        var top = ellipse.y - ellipse.height / 2
        var right = ellipse.x + ellipse.width / 2
        var bottom = ellipse.y + ellipse.height / 2

        if(ellipse.fill != "none"){
            this.getFillEllipse().setBounds(left , top , right , bottom )
            this.getFillEllipse().paint.set(this.fillPaint)
        }
        if(ellipse.stroke != "none"){
            this.getBorderEllipse().setBounds(left, top, right, bottom)
            this.getBorderEllipse().paint.set(this.borderPaint)
        }
        this.getEllipseDrawable().setBounds(left, top, right, bottom)
        DrawingObjectManager.addCommand(ellipse.id, this)
        CanvasUpdateService.invalidate()
    }

//    fun scaleTest(){
//        var quadrant = 3
//        var ratio = 2
//        var currentPathBoundsRectF = RectF()
//
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
//    }
}