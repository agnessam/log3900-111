package com.example.colorimagemobile.classes.toolsCommand

import android.R.attr
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.RectangleData
import com.example.colorimagemobile.models.RectangleUpdate
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import java.lang.Integer.min
import android.R.attr.path




class RectangleCommand(rectangleData: RectangleData): ICommand {
    private var boundingRectangle = Rect(0,0, CanvasService.extraCanvas.width, CanvasService.extraCanvas.height)

    private var startingPoint: Point? = null
    private var endingPoint: Point? = null

    private var layerIndex: Int = -1
    private var fillRectangleIndex: Int = -1
    private var borderRectangleIndex: Int = -1

    var rectangle: RectangleData = rectangleData
    private var rectangleShape: LayerDrawable
    private var borderPaint: Paint = Paint()
    private var fillPaint: Paint = Paint()
    init{

        var borderRectangle = ShapeDrawable(RectShape())
        var fillRectangle = ShapeDrawable(RectShape())
        var rectangleShapeArray = arrayOf<Drawable>()
        rectangleShape = LayerDrawable(rectangleShapeArray)
        fillRectangleIndex = rectangleShape.addLayer(fillRectangle)
        borderRectangleIndex = rectangleShape.addLayer(borderRectangle)
        layerIndex = CanvasService.layerDrawable.addLayer(rectangleShape)

        // Add layerIndex with id to CanvasService
        CanvasService.addNewDrawableToDrawing(rectangleData.id, layerIndex)

        borderPaint.color = if(rectangleData.stroke != "none") ColorService.rgbaToInt(rectangleData.stroke)
            else Color.WHITE
        fillPaint.color = if(rectangleData.fill != "none") ColorService.rgbaToInt(rectangleData.fill)
            else Color.BLACK

        borderPaint.style = Paint.Style.STROKE
        fillPaint.style = Paint.Style.FILL

        borderPaint.strokeJoin = Paint.Join.MITER
        borderPaint.strokeWidth = this.rectangle.strokeWidth.toFloat()


        setStartPoint(Point(rectangle.x.toFloat(), rectangle.y.toFloat()))
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
    }

    private fun getFillRectangle(): ShapeDrawable{
        return rectangleShape.getDrawable(fillRectangleIndex) as ShapeDrawable
    }

    private fun getBorderRectangle(): ShapeDrawable{
        return rectangleShape.getDrawable(borderRectangleIndex) as ShapeDrawable
    }

    private fun getRectangleDrawable(): LayerDrawable{
        return CanvasService.layerDrawable.getDrawable(this.layerIndex) as LayerDrawable
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
        var left = rectangle.x
        var top = rectangle.y
        var right = rectangle.x + rectangle.width
        var bottom = rectangle.y + rectangle.height

        if(rectangle.fill != "none"){
            this.getFillRectangle().setBounds(left , top , right , bottom)
            this.getFillRectangle().paint.set(this.fillPaint)
        }
        if(rectangle.stroke != "none"){
            this.getBorderRectangle().setBounds(left, top, right, bottom)
            this.getBorderRectangle().paint.set(this.borderPaint)
        }
        this.getRectangleDrawable().setBounds(left , top , right , bottom)
        CanvasUpdateService.invalidate()
    }

    fun scaleTest(){
    }
}