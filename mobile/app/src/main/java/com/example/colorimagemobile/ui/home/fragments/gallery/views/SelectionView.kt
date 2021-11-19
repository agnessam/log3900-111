package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import com.example.colorimagemobile.classes.toolsCommand.*
import com.example.colorimagemobile.models.RectangleData
import com.example.colorimagemobile.models.SelectionData
import com.example.colorimagemobile.models.ToolData
import com.example.colorimagemobile.services.UUIDService
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.SelectionService
import com.example.colorimagemobile.services.drawing.SelectionService.selectedShape
import com.example.colorimagemobile.services.drawing.SelectionService.selectedShapeIndex
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import com.example.colorimagemobile.services.socket.DrawingSocketService
import kotlin.math.abs

class SelectionView(context: Context?): CanvasView(context) {
    private var selectionCommand: SelectionCommand? = null
    private var translationCommand: TranslateCommand? = null

    override fun createPathObject() {
        // for sync
        val id = UUIDService.generateUUID()
        var selectionData = SelectionData(
            id = id,
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

    private fun drawBoundingBox(path: Path, strokeWidth: Int?): Boolean {
        var isInsidePath: Boolean
        var boundingBox: RectF
        boundingBox = getPathBoundingBox(path)
        isInsidePath = boundingBox.contains(motionTouchEventX,motionTouchEventY)
        if (isInsidePath) {
            createPathObject()
            setSelectionBounds(
                boundingBox.left.toInt(),
                boundingBox.top.toInt(),
                boundingBox.right.toInt(),
                boundingBox.bottom.toInt(),
                strokeWidth
            )
            selectionCommand!!.execute()
        }
        return isInsidePath
    }

    override fun onTouchDown() {
        CanvasService.extraCanvas.save()

        SelectionService.clearSelection()
        val numberOfLayers = DrawingObjectManager.numberOfLayers
        for (index in numberOfLayers - 1 downTo 0) {
            val drawable = DrawingObjectManager.getDrawable(index)
            val command = DrawingObjectManager.getCommand(index)
            var isInsidePath = false
            when(command) {
                is PencilCommand -> {
                    isInsidePath = drawBoundingBox(command.path, command.pencil.strokeWidth)
                }
                is RectangleCommand -> {
                    isInsidePath = drawBoundingBox(command.borderPath, null)
                }
                is EllipseCommand -> {
                    isInsidePath = drawBoundingBox(command.borderPath, null)
                }
            }

            if (isInsidePath) {
                selectedShape = drawable
                selectedShapeIndex = index
                DrawingSocketService.sendStartSelectionCommand(selectionCommand!!.selectionData, "SelectionStart")
                break
            } else {
                selectedShapeIndex = -1
                selectionCommand!!.execute()
            }
        }
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun translate(path1: Path, path2: Path?, dx: Float, dy: Float, strokeWidth: Int?) {
        // Use translation matrix to change path
        val translationMatrix = Matrix()
        translationMatrix.setTranslate(dx, dy)
        path1.transform(translationMatrix)
        path2?.transform(translationMatrix)

        // Draw selection box
        var boundingBox = getPathBoundingBox(path1)
        setSelectionBounds(
            boundingBox.left.toInt(),
            boundingBox.top.toInt(),
            boundingBox.right.toInt(),
            boundingBox.bottom.toInt(),
            strokeWidth
        )
        translationCommand!!.execute()
    }

    override fun onTouchMove() {
        // Translate only if touched inside of shape
        // TODO: Refactor
        translationCommand = TranslateCommand()

        val dx = motionTouchEventX - currentX
        val dy = motionTouchEventY - currentY

        if (abs(dx) >= touchTolerance || abs(dy) >= touchTolerance) {
            currentX = motionTouchEventX
            currentY = motionTouchEventY

            translationCommand!!.setTransformation(dx.toInt(), dy.toInt())
            SelectionService.clearSelection()
            if (selectedShapeIndex == -1) { return }
            val command = DrawingObjectManager.getCommand(selectedShapeIndex)
            when (command) {
                is PencilCommand -> {
                    translate(command.path, null, dx, dy, command.pencil.strokeWidth)
                }
                is RectangleCommand -> {
                    translate(command.borderPath, command.fillPath, dx, dy, null)
                }
                is EllipseCommand -> {
                    translate(command.borderPath, command.fillPath, dx, dy, null)
                }
            }
        }
    }

    override fun onTouchUp() {
//        TODO("Not yet implemented")
    }
}