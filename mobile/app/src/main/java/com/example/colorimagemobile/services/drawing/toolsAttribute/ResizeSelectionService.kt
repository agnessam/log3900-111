package com.example.colorimagemobile.services.drawing.toolsAttribute

import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import com.example.colorimagemobile.classes.toolsCommand.ResizeCommand
import com.example.colorimagemobile.services.drawing.AnchorIndexes
import com.example.colorimagemobile.services.drawing.SelectionService

object ResizeSelectionService {

    var resizeCommand: ResizeCommand? = null

    fun createResizeCommand(drawable:Drawable): ResizeCommand?{
        return ResizeCommand(drawable)
    }

    fun resize(motionTouchEventX: Float, motionTouchEventY: Float, oldRect: Rect){
        if(resizeCommand != null){
            var scaleReturn = IScale(1f, 1f, 0f, 0f)

            scaleReturn = when(SelectionService.selectedAnchorIndex){ // TODO("Fix bug with inversion not being done properly")
                AnchorIndexes.NONE -> scaleReturn
                AnchorIndexes.TOP_LEFT -> topLeftResize(motionTouchEventX, motionTouchEventY, oldRect)
                AnchorIndexes.TOP -> topResize(motionTouchEventX, motionTouchEventY,oldRect)
                AnchorIndexes.TOP_RIGHT -> topRightResize(motionTouchEventX, motionTouchEventY,oldRect)
                AnchorIndexes.LEFT -> leftResize(motionTouchEventX, motionTouchEventY,oldRect)
                AnchorIndexes.RIGHT -> rightResize(motionTouchEventX, motionTouchEventY,oldRect)
                AnchorIndexes.BOTTOM_LEFT -> bottomLeftResize(motionTouchEventX, motionTouchEventY,oldRect)
                AnchorIndexes.BOTTOM -> bottomResize(motionTouchEventX, motionTouchEventY,oldRect)
                AnchorIndexes.BOTTOM_RIGHT -> bottomRightResize(motionTouchEventX, motionTouchEventY,oldRect)
            }

            resizeCommand!!.setScales(scaleReturn.xScale, scaleReturn.yScale, scaleReturn.xTranslate, scaleReturn.yTranslate)
            resizeCommand!!.execute()
        }
    }

    private fun returnScales(oldRect: Rect, newRect: RectF): IScale{
        var newWidth = newRect.width()
        var newHeight = newRect.height()
        var originalWidth = oldRect.width()
        var originalHeight = oldRect.height()

        var xScale = newWidth / originalWidth
        var yScale = newHeight / originalHeight
        var xOffSet = newRect.left
        var yOffSet = newRect.right

        return IScale(xScale, yScale, xOffSet, yOffSet)
    }

    private fun topLeftResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: Rect): IScale{
        var newLeft = motionTouchEventX
        var newRight = oldRect.right.toFloat()
        var newTop = motionTouchEventY
        var newBottom = oldRect.bottom.toFloat()
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        return returnScales(oldRect, newRect)
    }

    private fun topResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: Rect): IScale{
        var newLeft = oldRect.left.toFloat()
        var newRight = oldRect.right.toFloat()
        var newTop = motionTouchEventY
        var newBottom = oldRect.bottom.toFloat()

        var newRect = RectF(newLeft, newTop, newRight, newBottom)
        return returnScales(oldRect, newRect)
    }

    private fun topRightResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: Rect): IScale{ // TODO pretty much same as top left resize so must modify somehow
        var newLeft = oldRect.right.toFloat()
        var newRight = motionTouchEventX
        var newTop = motionTouchEventY
        var newBottom = oldRect.bottom.toFloat()
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        return returnScales(oldRect, newRect)

    }

    private fun leftResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: Rect): IScale{
        var newLeft = motionTouchEventX
        var newRight = oldRect.right.toFloat()
        var newTop = oldRect.bottom.toFloat()
        var newBottom = oldRect.bottom.toFloat()
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        return returnScales(oldRect, newRect)

    }

    private fun rightResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: Rect): IScale{
        var newLeft = oldRect.left.toFloat()
        var newRight =  motionTouchEventX
        var newTop = oldRect.bottom.toFloat()
        var newBottom = oldRect.bottom.toFloat()
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        return returnScales(oldRect, newRect)
    }

    private fun bottomLeftResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: Rect): IScale{
        var newLeft = motionTouchEventX
        var newRight = oldRect.right.toFloat()
        var newTop = oldRect.bottom.toFloat()
        var newBottom = motionTouchEventY
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        return returnScales(oldRect, newRect)
    }

    private fun bottomResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: Rect): IScale{
        var newLeft = oldRect.left.toFloat()
        var newRight = oldRect.right.toFloat()
        var newTop = oldRect.top.toFloat()
        var newBottom = motionTouchEventY
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        return returnScales(oldRect, newRect)
    }

    private fun bottomRightResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: Rect): IScale{
        var newLeft = oldRect.left.toFloat()
        var newRight = motionTouchEventX
        var newTop = oldRect.top.toFloat()
        var newBottom = motionTouchEventY
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        return returnScales(oldRect, newRect)
    }


    private data class IScale (
        var xScale: Float,
        var yScale: Float,
        var xTranslate: Float,
        var yTranslate: Float,
    )
}
