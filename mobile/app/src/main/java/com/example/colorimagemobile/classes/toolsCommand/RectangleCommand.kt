package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.RectangleData
import com.example.colorimagemobile.models.RectangleUpdate
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import java.lang.Integer.min

class RectangleCommand(rectangleData: RectangleData): ICommand {
    private var boundingRectangle = Rect(0,0, CanvasService.extraCanvas.width, CanvasService.extraCanvas.height)

    private var startingPoint: Point? = null
    private var endingPoint: Point? = null

    private var layerIndex: Int = -1
    private var fillRectangleIndex: Int = -1
    private var borderRectangleIndex: Int = -1

    var rectangle: RectangleData = rectangleData

    private lateinit var rectangleShape: LayerDrawable

    private var borderPaint: Paint = Paint()
    private var fillPaint: Paint = Paint()

    var borderPath = Path()
    var fillPath = Path()

    init{
        var fillRectangle = createNewRectangle()
        var borderRectangle = createNewRectangle()

        initializeRectangleLayers(fillRectangle, borderRectangle)
        borderPaint = initializePaint(rectangleData.stroke, Color.WHITE)
        fillPaint = initializePaint(rectangleData.fill, Color.BLACK)

        setStartPoint(Point(rectangle.x.toFloat(), rectangle.y.toFloat()))
    }

    private fun initializePaint(color: String, defaultColor: Int): Paint{
        var paint = Paint()
        paint.color = if(color != "none") ColorService.rgbaToInt(color)
        else defaultColor
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.isDither = true
        return paint
    }

    private fun initializeRectangleLayers(fillRectangle: ShapeDrawable, borderRectangle: ShapeDrawable) {
        var rectangleShapeArray = arrayOf<Drawable>()
        rectangleShape = LayerDrawable(rectangleShapeArray)

        fillRectangleIndex = rectangleShape.addLayer(fillRectangle)
        borderRectangleIndex = rectangleShape.addLayer(borderRectangle)
        layerIndex = DrawingObjectManager.addLayer(rectangleShape, rectangle.id)
    }

    private fun createNewRectangle(): ShapeDrawable{
        val pathShape = PathShape(Path(),
            CanvasService.extraCanvas.width.toFloat(), CanvasService.extraCanvas.height.toFloat())
        return ShapeDrawable(pathShape)
    }

    private fun setStartPoint(startPoint: Point) {
        startingPoint = startPoint
    }

    fun setEndPoint(endPoint: Point) {
        endingPoint = endPoint

        rectangle.width = kotlin.math.abs(endingPoint!!.x - startingPoint!!.x).toInt()
        rectangle.x = min(endingPoint!!.x.toInt(), startingPoint!!.x.toInt())
        rectangle.height = kotlin.math.abs(endingPoint!!.y - startingPoint!!.y).toInt()
        rectangle.y = min(endingPoint!!.y.toInt(), startingPoint!!.y.toInt())

        this.generateBorderPath()
        this.generateFillPath()
    }

    private fun getFillRectangle(): ShapeDrawable{
        return rectangleShape.getDrawable(fillRectangleIndex) as ShapeDrawable
    }

    private fun getBorderRectangle(): ShapeDrawable{
        return rectangleShape.getDrawable(borderRectangleIndex) as ShapeDrawable
    }

    private fun getRectangleDrawable(): LayerDrawable{
        return DrawingObjectManager.getDrawable(this.layerIndex) as LayerDrawable
    }

    override fun update(drawingCommand: Any) {
        if(drawingCommand is RectangleUpdate){
            rectangle.x = drawingCommand.x
            rectangle.y = drawingCommand.y
            rectangle.width = drawingCommand.width
            rectangle.height = drawingCommand.height
        }
    }

    override fun execute() {
        if(rectangle.stroke != "none"){
            val borderRectPathShape = PathShape(borderPath,
                CanvasService.extraCanvas.width.toFloat(), CanvasService.extraCanvas.height.toFloat()
            )
            var borderRectDrawable = ShapeDrawable(borderRectPathShape)
            this.getBorderRectangle().bounds = this.boundingRectangle

            this.getRectangleDrawable().setDrawable(this.borderRectangleIndex, borderRectDrawable)
            this.getBorderRectangle().paint.set(this.borderPaint)
        }

        if(rectangle.fill != "none"){
            val fillRectPathShape = PathShape(fillPath,
                CanvasService.extraCanvas.width.toFloat(), CanvasService.extraCanvas.height.toFloat()
            )

            var fillRectDrawable = ShapeDrawable(fillRectPathShape)
            this.getFillRectangle().bounds = this.boundingRectangle

            this.getRectangleDrawable().setDrawable(this.fillRectangleIndex, fillRectDrawable)
            this.getFillRectangle().paint.set(this.fillPaint)
        }

        DrawingObjectManager.setDrawable(layerIndex, rectangleShape)
        CanvasUpdateService.invalidate()
    }

    private fun generateFillPath(){
        var left = rectangle.x
        var top = rectangle.y
        var right = rectangle.x + rectangle.width
        var bottom = rectangle.y + rectangle.height
        var rect = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

        fillPath = Path()
        fillPath.addRect(rect, Path.Direction.CW)
    }

    private fun generateBorderPath(){
        var left = rectangle.x
        var top = rectangle.y
        var right = rectangle.x + rectangle.width
        var bottom = rectangle.y + rectangle.height
        var rect = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

        borderPath = Path()

        borderPath.addRect(rect, Path.Direction.CW)
        borderPath.close()

        val innerRect = RectF(rect)
        innerRect.inset(this.rectangle.strokeWidth.toFloat(), this.rectangle.strokeWidth.toFloat())
        if (innerRect.width() > 0 && innerRect.height() > 0) {
            borderPath.addRect(innerRect, Path.Direction.CW)
            borderPath.close()
        }

        borderPath.fillType = Path.FillType.EVEN_ODD
    }
}