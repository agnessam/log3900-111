package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.services.drawing.*
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

class PencilCommand(paintPath: PaintPath, layerIndex: Int): ICommand {
    private val pencilPaintPath: PaintPath = paintPath
    private var layerIndex: Int = layerIndex
    private var boundingRectangle = Rect(0, 0, CanvasService.extraCanvas.width, CanvasService.extraCanvas.height)

    fun addPoint(point: Point) {
        pencilPaintPath.points.add(point)
        PathService.addPaintPath(pencilPaintPath)
    }

    // for synchro
    override fun update(drawingCommand: SyncUpdate) {
        pencilPaintPath.path.lineTo(drawingCommand.point.x, drawingCommand.point.y)
        addPoint(drawingCommand.point)
    }

    private fun getPathDrawable(): ShapeDrawable {
        return CanvasService.layerDrawable.getDrawable(this.layerIndex) as ShapeDrawable
    }

    private fun getDimensions(path: Path): RectF {
        val rectF = RectF()
        path.computeBounds(rectF, true)
        return rectF
    }

    // update canvas
    override fun execute() {
        this.getPathDrawable().setBounds( this.boundingRectangle)
        this.getPathDrawable().paint.set(pencilPaintPath.brush.getPaint())
        CanvasUpdateService.invalidate()
    }
}