package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.RectangleData
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService

class RectangleCommand(rectangleData: RectangleData): ICommand {
    private var startingPoint: Point? = null
    private var endingPoint: Point? = null

    private var layerIndex: Int = -1
    private var fillRectangleIndex: Int = -1
    private var borderRectangleIndex: Int = -1

    private var rectangle: RectangleData = rectangleData
    private lateinit var rectangleShape: LayerDrawable
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

        borderPaint.color = Color.WHITE // TODO put secondary color here
        fillPaint.color = ColorService.getPrimaryColorAsInt() // TODO put primary color here

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
        var startX = startingPoint!!.x.toInt()
        var startY = startingPoint!!.y.toInt()
        var endX = endingPoint!!.x.toInt()
        var endY = endingPoint!!.y.toInt()


        if(startX > endX){
            rectangle.width = startX - endX
            rectangle.x = endX
        }
        else{
            rectangle.x = startX
            rectangle.width = endX - startX
        }

        if(startY > endY){
            rectangle.y = endY
            rectangle.height = startY - endY
        }
        else{
            rectangle.y = startY
            rectangle.height = endY - startY
        }
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

    override fun update(drawingCommand: SyncUpdate) {
        TODO("Not yet implemented")
    }

    override fun execute() {

        var left = rectangle.x
        var top = rectangle.y
        var right = rectangle.x + rectangle.width
        var bottom = rectangle.y + rectangle.height

        if(rectangle.fill != "none"){
            this.getFillRectangle().setBounds(left , top , right , bottom )
            this.getFillRectangle().paint.set(this.fillPaint)
        }
        if(rectangle.stroke != "none"){
            this.getBorderRectangle().setBounds(left, top, right, bottom)
            this.getBorderRectangle().paint.set(this.borderPaint)
        }
        CanvasUpdateService.invalidate()
    }
}