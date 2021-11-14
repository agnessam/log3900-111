package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.EllipseData
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

class EllipseCommand(ellipseData: EllipseData, layerIndex:Int): ICommand {
    private var startingPoint: Point? = null
    private var endingPoint: Point? = null
    private var layerIndex: Int = layerIndex
    private var ellipse: EllipseData = ellipseData
    private var paint: Paint = Paint()

    init{
        paint.color = ColorService.getColorAsInt()
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = this.ellipse.strokeWidth.toFloat()
        setStartPoint(Point(ellipse.x.toFloat(), ellipse.y.toFloat()))
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
            ellipse.width = startX - endX
            ellipse.x = endX
        }
        else{
            ellipse.x = startX
            ellipse.width = endX - startX
        }

        if(startY > endY){
            ellipse.y = endY
            ellipse.height = startY - endY
        }
        else{
            ellipse.y = startY
            ellipse.height = endY - startY
        }
    }

    fun getEllipseDrawable(): ShapeDrawable{
        return CanvasService.layerDrawable.getDrawable(this.layerIndex) as ShapeDrawable
    }

    override fun update(drawingCommand: SyncUpdate) {
        TODO("Not yet implemented")
    }

    override fun execute() {
//        printMsg()
        var left = ellipse.x
        var top = ellipse.y
        var right = ellipse.x + ellipse.width
        var bottom = ellipse.y + ellipse.height
        this.getEllipseDrawable().setBounds(left, top, right, bottom)
        this.getEllipseDrawable().paint.set(this.paint)
        CanvasUpdateService.invalidate()
    }
}