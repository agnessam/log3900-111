package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import com.example.colorimagemobile.classes.toolsCommand.*
import com.example.colorimagemobile.models.SelectionData
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.SynchronisationService
import com.example.colorimagemobile.services.socket.DrawingSocketService
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService.selectedShape
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService.selectedShapeIndex
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService
import com.example.colorimagemobile.services.drawing.toolsAttribute.AnchorIndexes
import com.example.colorimagemobile.services.drawing.toolsAttribute.ResizeSelectionService
import kotlin.math.abs

class SelectionView(context: Context?): CanvasView(context) {
    private var selectionCommand: SelectionCommand? = null
    private var translationCommand: TranslateCommand? = null

    override fun createPathObject() {
        // for sync
        val id = DrawingObjectManager.getUuid(selectedShapeIndex) ?: return
        var selectionData = SelectionData(
            id = id
        )
        selectionCommand = SelectionCommand(selectionData)
    }

    private fun getPathBoundingBox(path: Path): RectF {
        val rectF = RectF()
        path.computeBounds(rectF, true)
        return rectF
    }

    private fun setSelectionBounds(left: Int, top: Int, right: Int, bottom: Int, strokeWidth: Int?) {
        if (strokeWidth != null) {
            SelectionService.setSelectionBounds(
                (left - strokeWidth / 2),
                (top - strokeWidth / 2),
                (right + strokeWidth / 2),
                (bottom + strokeWidth / 2)
            )
        } else {
            SelectionService.setSelectionBounds(left, top, right, bottom)
        }
    }

    private fun drawBoundingBox(drawable: Drawable, index: Int, path: Path, strokeWidth: Int?): Boolean {
        var isInsidePath: Boolean
        var boundingBox = getPathBoundingBox(path)
        isInsidePath = boundingBox.contains(motionTouchEventX,motionTouchEventY)

        // Check if shape is in preview shapes. If it is
        var uuid = DrawingObjectManager.getUuid(index)
        var isInPreviewShapes = SynchronisationService.isShapeInPreview(uuid)
        if (isInsidePath && !isInPreviewShapes) {
            selectedShape = drawable
            selectedShapeIndex = index
            createPathObject()
            setSelectionBounds(
                boundingBox.left.toInt(),
                boundingBox.top.toInt(),
                boundingBox.right.toInt(),
                boundingBox.bottom.toInt(),
                strokeWidth
            )
            DrawingSocketService.sendStartSelectionCommand(selectionCommand!!.selectionData, "SelectionStart")
        } else {
            selectedShapeIndex = -1
            if(selectionCommand != null){
                DrawingSocketService.sendConfirmSelectionCommand(selectionCommand!!.selectionData, "SelectionStart")
            }
        }
        CanvasUpdateService.invalidate()
        return isInsidePath
    }

    override fun onTouchDown() {
        CanvasService.extraCanvas.save()

        if (SelectionService.getSelectedAnchor(
                motionTouchEventX,
                motionTouchEventY
            ) != AnchorIndexes.NONE
        ) {
            SelectionService.setSelectedAnchor(motionTouchEventX, motionTouchEventY)
            return
        }

        createPathObject()
        SelectionService.clearSelection()
        val numberOfLayers = DrawingObjectManager.numberOfLayers
        for (index in numberOfLayers - 1 downTo 0) {
            val drawable = DrawingObjectManager.getDrawable(index)
            val command = DrawingObjectManager.getCommand(index)
            var isInsidePath = false
            when(command) {
                is PencilCommand -> {
                    isInsidePath = drawBoundingBox(drawable, index, command.path, command.pencil.strokeWidth)
                }
                is RectangleCommand -> {
                    isInsidePath = drawBoundingBox(drawable, index, command.borderPath, null)
                }
                is EllipseCommand -> {
                    isInsidePath = drawBoundingBox(drawable, index, command.borderPath, null)
                }
            }
            if(isInsidePath) break
        }
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun resetBoundingBox() {
        SelectionService.clearSelection()
        val command = DrawingObjectManager.getCommand(selectedShapeIndex)
        var path: Path? = null
        var strokeWidth: Int? = null
        when(command) {
            is PencilCommand -> {
                path = command.path
                strokeWidth = command.pencil.strokeWidth
            }
            is RectangleCommand -> {
                path = command.borderPath
                strokeWidth = null
            }
            is EllipseCommand -> {
                path = command.borderPath
                strokeWidth = null
            }
        }
        if(path != null){
            var boundingBox = getPathBoundingBox(path)
            setSelectionBounds(
                boundingBox.left.toInt(),
                boundingBox.top.toInt(),
                boundingBox.right.toInt(),
                boundingBox.bottom.toInt(),
                strokeWidth
            )
        }
    }

    override fun onTouchMove() {
        // Resizing: touching one of 8 points on bounding box
        if (SelectionService.selectedAnchorIndex != AnchorIndexes.NONE
        ) {
            if (selectedShapeIndex != -1) {
                ResizeSelectionService.onTouchMove(motionTouchEventX, motionTouchEventY)
                resetBoundingBox()
                return
            }
        }

        // Translate only if touched inside of shape
        val dx = motionTouchEventX - currentX
        val dy = motionTouchEventY - currentY

        if (abs(dx) >= touchTolerance || abs(dy) >= touchTolerance) {
            currentX = motionTouchEventX
            currentY = motionTouchEventY

            if (selectedShapeIndex == -1) { return }
            translationCommand = TranslateCommand(selectedShapeIndex)
            translationCommand!!.setTransformation(dx, dy)
            translationCommand!!.execute()

            resetBoundingBox()
        }
    }

    override fun onTouchUp() {
        ResizeSelectionService.onTouchUp()
    }
}