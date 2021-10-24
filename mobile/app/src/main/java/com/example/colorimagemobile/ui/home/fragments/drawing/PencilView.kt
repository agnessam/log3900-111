package com.example.colorimagemobile.ui.home.fragments.drawing

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import com.example.colorimagemobile.utils.CommonFun

class PencilView(context: Context?): CanvasView(context) {

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        extraCanvas.drawColor(Color.rgb(150, 150, 150))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        CommonFun.printMsg("PENCILL")

    }
}