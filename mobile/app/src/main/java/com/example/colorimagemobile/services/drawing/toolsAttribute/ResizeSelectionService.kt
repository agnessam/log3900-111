package com.example.colorimagemobile.services.drawing.toolsAttribute

import android.graphics.*
import com.example.colorimagemobile.classes.toolsCommand.EllipseCommand
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.classes.toolsCommand.RectangleCommand
import com.example.colorimagemobile.classes.toolsCommand.ResizeCommand
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.services.drawing.AnchorIndexes
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.SelectionService
import com.example.colorimagemobile.services.drawing.SelectionService.getPathBoundingBox

object ResizeSelectionService {

    var resizeCommand: ResizeCommand? = null

    private fun createResizeCommand(objectId:String): ResizeCommand?{
        return ResizeCommand(objectId)
    }

    private fun resize(motionTouchEventX: Float, motionTouchEventY: Float, oldRect: RectF){
        if(resizeCommand != null){
            var scaleReturn = ResizeData(IScale(1f, 1f), PointF(0f, 0f))

            var resizeData = when(SelectionService.selectedAnchorIndex){
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

            resizeCommand!!.setScales(resizeData.scale.xScale, resizeData.scale.yScale, resizeData.anchorPoint.x, resizeData.anchorPoint.y)
            resizeCommand!!.execute()
        }
    }

    private fun getScales(oldRect: RectF, newRect: RectF): IScale{
        var newWidth = newRect.width()
        var newHeight = newRect.height()
        var originalWidth = oldRect.width()
        var originalHeight = oldRect.height()

        var xScale = newWidth / originalWidth
        var yScale = newHeight / originalHeight

        return IScale(xScale, yScale)
    }

    private fun topLeftResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: RectF): ResizeData{
        var newLeft = motionTouchEventX
        var newRight = oldRect.right
        var newTop = motionTouchEventY
        var newBottom = oldRect.bottom
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        var scale = getScales(oldRect, newRect)
        var anchorPoint = PointF(newRight, newBottom)
        return ResizeData(scale, anchorPoint)
    }

    private fun topResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: RectF): ResizeData{
        var newLeft = oldRect.left
        var newRight = oldRect.right
        var newTop = motionTouchEventY
        var newBottom = oldRect.bottom
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        var centerX = (newRight + newLeft) / 2

        var scale = getScales(oldRect, newRect)
        var anchorPoint = PointF(centerX, newBottom)
        return ResizeData(scale, anchorPoint)
    }

    private fun topRightResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: RectF): ResizeData{
        var newLeft = oldRect.left
        var newRight = motionTouchEventX
        var newTop = motionTouchEventY
        var newBottom = oldRect.bottom
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        var scale = getScales(oldRect, newRect)
        var anchorPoint = PointF(newLeft, newBottom)
        return ResizeData(scale, anchorPoint)
    }

    private fun leftResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: RectF): ResizeData{
        var newLeft = motionTouchEventX
        var newRight = oldRect.right
        var newTop = oldRect.top
        var newBottom = oldRect.bottom
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        var centerY = (newBottom + newTop) / 2

        var scale = getScales(oldRect, newRect)
        var anchorPoint = PointF(newRight, centerY)
        return ResizeData(scale, anchorPoint)
    }

    private fun rightResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: RectF): ResizeData{
        var newLeft = oldRect.left
        var newRight = motionTouchEventX
        var newTop = oldRect.top
        var newBottom = oldRect.bottom
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        var centerY = (newBottom + newTop) / 2

        var scale = getScales(oldRect, newRect)
        var anchorPoint = PointF(newLeft, centerY)
        return ResizeData(scale, anchorPoint)
    }

    private fun bottomLeftResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: RectF): ResizeData{
        var newLeft = motionTouchEventX
        var newRight = oldRect.right
        var newTop = oldRect.top
        var newBottom = motionTouchEventY
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        var scale = getScales(oldRect, newRect)
        var anchorPoint = PointF(newRight, newTop)
        return ResizeData(scale, anchorPoint)
    }

    private fun bottomResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: RectF): ResizeData{
        var newLeft = oldRect.left
        var newRight = oldRect.right
        var newTop = oldRect.top
        var newBottom = motionTouchEventY
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        var centerX = (newRight + newLeft) / 2

        var scale = getScales(oldRect, newRect)
        var anchorPoint = PointF(centerX, newTop)
        return ResizeData(scale, anchorPoint)
    }

    private fun bottomRightResize(motionTouchEventX: Float, motionTouchEventY:Float, oldRect: RectF): ResizeData{
        var newLeft = oldRect.left
        var newRight = motionTouchEventX
        var newTop = oldRect.top
        var newBottom = motionTouchEventY
        var newRect = RectF(newLeft, newTop, newRight, newBottom)

        var scale = getScales(oldRect, newRect)
        var anchorPoint = PointF(newLeft, newTop)
        return ResizeData(scale, anchorPoint)
    }

    fun onTouchMove(motionTouchEventX: Float, motionTouchEventY: Float) {
        var command: ICommand = DrawingObjectManager.getCommand(SelectionService.selectedShapeIndex)
            ?: return

        if (resizeCommand == null) {
            var objectId = when(command) {
                is PencilCommand -> command.pencil.id
                is EllipseCommand -> command.ellipse.id
                is RectangleCommand -> command.rectangle.id
                else -> return
            }

            resizeCommand = createResizeCommand(objectId)
        }
        var pathRect = when(command){
            is PencilCommand -> getPathBoundingBox(command.path)
            is EllipseCommand -> getPathBoundingBox(command.borderPath)
            is RectangleCommand -> getPathBoundingBox(command.borderPath)
            else -> return
        }
        resize(motionTouchEventX, motionTouchEventY, RectF(pathRect.bounds))
    }


    private data class IScale (
        var xScale: Float,
        var yScale: Float
    )

    private data class ResizeData(
        var scale: IScale,
        var anchorPoint: PointF
    )
}
