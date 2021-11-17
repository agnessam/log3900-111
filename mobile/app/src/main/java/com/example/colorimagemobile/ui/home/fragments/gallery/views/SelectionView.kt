package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import com.example.colorimagemobile.classes.toolsCommand.*
import com.example.colorimagemobile.models.SelectionData
import com.example.colorimagemobile.services.UUIDService
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.SelectionService
import com.example.colorimagemobile.services.drawing.SelectionService.selectedShape
import com.example.colorimagemobile.services.drawing.SelectionService.selectedShapeIndex
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import kotlin.math.abs

class SelectionView(context: Context?): CanvasView(context) {
    private var selectionCommand: SelectionCommand? = null
    private var translationCommand: TranslateCommand? = null

    override fun createPathObject() {
        selectionCommand = SelectionCommand()

        // for sync
        val id = UUIDService.generateUUID()
        var selectionData = SelectionData(
            id = id,
        )
    }

    private fun getPathBoundingBox(path: Path): RectF {
        val rectF = RectF()
        path.computeBounds(rectF, true)
        return rectF
    }

    private fun setSelectionBounds(left: Int, top: Int, right: Int, bottom: Int, strokeWidth: Int) {
        SelectionService.setSelectionBounds(
            (left - strokeWidth / 2),
            (top - strokeWidth / 2),
            (right + strokeWidth / 2),
            (bottom + strokeWidth / 2)
        )
    }

    override fun onTouchDown() {
        CanvasService.extraCanvas.save()
        createPathObject()
        SelectionService.clearSelection()
        val numberOfLayers = DrawingObjectManager.numberOfLayers
        for (index in numberOfLayers - 1 downTo 0) {
            val drawable = DrawingObjectManager.getDrawable(index)
            val command = DrawingObjectManager.getCommand(index)
            var isInsidePath: Boolean
            var boundingBox = RectF()
            when(command) {
                is PencilCommand -> {
                    boundingBox = getPathBoundingBox(command.path)
                    isInsidePath = boundingBox.contains(motionTouchEventX,motionTouchEventY)
                    if (isInsidePath) {
                        setSelectionBounds(
                            boundingBox.left.toInt(),
                            boundingBox.top.toInt(),
                            boundingBox.right.toInt(),
                            boundingBox.bottom.toInt(),
                            (drawable as ShapeDrawable).paint.strokeWidth.toInt()
                        )
                    }
                }
                is RectangleCommand -> {
                    if (command.rectangle.stroke == "none" && command.rectangle.fill != "none") {
                        boundingBox = getPathBoundingBox(command.fillPath)
                    } else {
                        boundingBox = getPathBoundingBox(command.borderPath)
                    }

                    isInsidePath = boundingBox.contains(motionTouchEventX,motionTouchEventY)
                    if (isInsidePath) {
                        setSelectionBounds(
                            boundingBox.left.toInt(),
                            boundingBox.top.toInt(),
                            boundingBox.right.toInt(),
                            boundingBox.bottom.toInt(),
                            command.rectangle.strokeWidth
                        )
                    }
                }
                is EllipseCommand -> {
                    if (command.ellipse.stroke == "none" && command.ellipse.fill != "none") {
                        boundingBox = getPathBoundingBox(command.fillPath)
                    } else {
                        boundingBox = getPathBoundingBox(command.borderPath)
                    }

                    isInsidePath = boundingBox.contains(motionTouchEventX,motionTouchEventY)
                    if (isInsidePath) {
                        setSelectionBounds(
                            boundingBox.left.toInt(),
                            boundingBox.top.toInt(),
                            boundingBox.right.toInt(),
                            boundingBox.bottom.toInt(),
                            command.ellipse.strokeWidth
                        )
                    }
                }
            }

            isInsidePath = boundingBox.contains(motionTouchEventX,motionTouchEventY)
            if (isInsidePath) {
                selectedShape = drawable
                selectedShapeIndex = index
                selectionCommand!!.execute()
                break
            } else {
                selectionCommand!!.execute()
                selectedShapeIndex = -1
            }
        }
        currentX = motionTouchEventX
        currentY = motionTouchEventY
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
            when (selectedShape) {
                is ShapeDrawable -> {
                    val savedBounds = selectedShape.bounds
                    val savedPaint = (selectedShape as ShapeDrawable).paint

                    // Use translation matrix to change path
                    val translationMatrix = Matrix()
                    translationMatrix.setTranslate(dx, dy)
                    val path = PencilService.paths[selectedShapeIndex]!!
                    path.transform(translationMatrix)
                    PencilService.paths[selectedShapeIndex] = path

                    // Reinstantiate shape using new path
                    selectedShape = ShapeDrawable(
                        PathShape(
                            path,
                            abs(savedBounds.bottom - savedBounds.top).toFloat(),
                            abs( savedBounds.right - savedBounds.left).toFloat()
                        )
                    )
                    (selectedShape as ShapeDrawable).paint.set(savedPaint)
                    selectedShape.setBounds(savedBounds)

                    // Draw selection box
                    var boundingBox = getPathBoundingBox(path)
                    val strokeWidth = savedPaint.strokeWidth.toInt()
//                    setSelectionBounds(boundingBox.bounds, strokeWidth)
                    translationCommand!!.execute()
                }
                is LayerDrawable -> {
                    val left = selectedShape.bounds.left + dx.toInt()
                    val top = selectedShape.bounds.top + dy.toInt()
                    val right = selectedShape.bounds.right + dx.toInt()
                    val bottom = selectedShape.bounds.bottom + dy.toInt()
                    selectedShape.setBounds(left, top, right, bottom)

                    val strokeWidth = ((selectedShape as LayerDrawable).getDrawable(1) as ShapeDrawable).paint.strokeWidth.toInt()
                    setSelectionBounds(
                        left,
                        top,
                        right,
                        bottom,
                        strokeWidth
                    )
                    translationCommand!!.execute()
                }
            }
        }
    }

    override fun onTouchUp() {
//        TODO("Not yet implemented")
    }
}