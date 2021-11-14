package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.EllipseData
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService

class EllipseCommand(ellipseData: EllipseData): ICommand {
    private var startingPoint: Point? = null
    private var endingPoint: Point? = null

    private var layerIndex: Int = -1
    private var fillEllipseIndex: Int = -1
    private var borderEllipseIndex: Int = -1

    private var ellipse: EllipseData = ellipseData
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
            layerIndex = CanvasService.layerDrawable.addLayer(ellipseShape)
        }
        borderPaint.color = Color.WHITE // TODO put secondary color here
        fillPaint.color = ColorService.getPrimaryColorAsInt() // TODO put primary color here

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

    private fun getFillEllipse(): ShapeDrawable{
        return ellipseShape.getDrawable(fillEllipseIndex) as ShapeDrawable
    }

    private fun getBorderEllipse(): ShapeDrawable{
        return ellipseShape.getDrawable(borderEllipseIndex) as ShapeDrawable
    }

    private fun getEllipseDrawable(): LayerDrawable {
        return CanvasService.layerDrawable.getDrawable(this.layerIndex) as LayerDrawable
    }

    override fun update(drawingCommand: Any) {
        TODO("Not yet implemented")
    }

    override fun execute() {
        var left = ellipse.x
        var top = ellipse.y
        var right = ellipse.x + ellipse.width
        var bottom = ellipse.y + ellipse.height

        if(ellipse.fill != "none"){
            this.getFillEllipse().setBounds(left , top , right , bottom )
            this.getFillEllipse().paint.set(this.fillPaint)
        }
        if(ellipse.stroke != "none"){
            this.getBorderEllipse().setBounds(left, top, right, bottom)
            this.getBorderEllipse().paint.set(this.borderPaint)
        }
        CanvasUpdateService.invalidate()
    }
}