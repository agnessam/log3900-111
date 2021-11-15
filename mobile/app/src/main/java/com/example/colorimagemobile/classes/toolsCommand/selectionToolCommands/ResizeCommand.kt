package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.drawable.Drawable
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

class ResizeCommand(drawable: Drawable) : ICommand {
    private var xScale: Float = 1f
    private var yScale: Float = 1f
    private var xTranslate: Float = 0f
    private var yTranslate: Float = 0f
    private var drawableToResize: Drawable = drawable

    fun setScales(xScale: Float, yScale: Float, xTranslate: Float, yTranslate: Float){
        this.xScale = xScale
        this.yScale = yScale
        this.xTranslate = xTranslate
        this.yTranslate = yTranslate
    }

    override fun execute() {
//        TODO("Not yet implemented")
        printMsg(drawableToResize.toString())
//        when(drawableToResize) ->

    }

    override fun update(drawingCommand: Any) {
//        TODO("Not yet implemented")
        printMsg(drawableToResize.toString())
    }

//    fun scalePencil()

}