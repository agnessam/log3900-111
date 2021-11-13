package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.PaintPath
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import kotlin.math.round

class RectangleCommand(paintPath: PaintPath, layerIndex:Int): ICommand {
    private val rectPaintPath: PaintPath = paintPath
    private var startingPoint: Point? = null
    private var endingPoint: Point? = null
    private var layerIndex: Int = layerIndex


    fun setStartPoint(startPoint: Point) {
        startingPoint = startPoint
    }

    fun setEndPoint(endPoint: Point) {
        endingPoint = endPoint
    }

    fun getRectangleDrawable(): ShapeDrawable{
        return CanvasService.layerDrawable.getDrawable(this.layerIndex) as ShapeDrawable
    }

    override fun update(drawingCommand: SyncUpdate) {
        TODO("Not yet implemented")
    }

    override fun execute() {
//        CanvasService.extraCanvas.drawRect(startingPoint!!.x, startingPoint!!.y, endingPoint!!.x, endingPoint!!.y, rectPaintPath.brush.getPaint())
//        CanvasUpdateService.invalidate()

//        var shapeDrawable: ShapeDrawable = ShapeDrawable(RectShape())

        var startX = startingPoint!!.x.toInt()
        var startY = startingPoint!!.y.toInt()
        var endX = endingPoint!!.x.toInt()
        var endY = endingPoint!!.y.toInt()

        var left = 0
        var right = 0
        var top = 0
        var bottom = 0
        if(startX > endX){
            right = startX
            left = endX
        }
        else{
            left = startX
            right = endX
        }

        if(startY > endY){
            top = endY
            bottom = startY
        }
        else{
            top = startY
            bottom = endY
        }
        this.getRectangleDrawable().setBounds(left, top, right, bottom)
        this.getRectangleDrawable().getPaint().setColor(Color.parseColor("#000000"))
        printMsg(this.getRectangleDrawable().getShape().toString())
        CanvasUpdateService.invalidate()
    }
}