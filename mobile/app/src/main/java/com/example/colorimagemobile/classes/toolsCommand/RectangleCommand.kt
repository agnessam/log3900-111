package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.RectangleData
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import kotlin.math.round

class RectangleCommand(rectangleData: RectangleData, layerIndex:Int): ICommand {
    private var startingPoint: Point? = null
    private var endingPoint: Point? = null
    private var layerIndex: Int = layerIndex
    private var rectangle: RectangleData = rectangleData
    private var paint: Paint = Paint()

    init{
        paint.color = ColorService.getColorAsInt()
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = this.rectangle.strokeWidth.toFloat()
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

    fun getRectangleDrawable(): ShapeDrawable{
        return CanvasService.layerDrawable.getDrawable(this.layerIndex) as ShapeDrawable
    }

    override fun update(drawingCommand: SyncUpdate) {
        TODO("Not yet implemented")
    }

    override fun execute() {
        var left = rectangle.x
        var top = rectangle.y
        var right = rectangle.x + rectangle.width
        var bottom = rectangle.y + rectangle.height
        this.getRectangleDrawable().setBounds(left, top, right, bottom)
        this.getRectangleDrawable().paint.set(this.paint)
        CanvasUpdateService.invalidate()
    }
}