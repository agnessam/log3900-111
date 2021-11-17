package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import com.example.colorimagemobile.classes.toolsCommand.SelectionCommand
import com.example.colorimagemobile.classes.toolsCommand.TranslateCommand
import com.example.colorimagemobile.models.SelectionData
import com.example.colorimagemobile.services.UUIDService
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService.selectedShape
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService.selectedShapeIndex
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
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

    private fun getPathBoundingBox(path: Path): Region {
        val rectF = RectF()
        path.computeBounds(rectF, true)
        var region = Region()
        region.setPath(
            path,
            Region(
                rectF.left.toInt(),
                rectF.top.toInt(),
                rectF.right.toInt(),
                rectF.bottom.toInt()
            )
        )

        return region
    }

    private fun setSelectionBounds(bounds: Rect, strokeWidth: Int) {
        SelectionService.setSelectionBounds(
            (bounds.left - strokeWidth / 2),
            (bounds.top - strokeWidth / 2),
            (bounds.right + strokeWidth / 2),
            (bounds.bottom + strokeWidth / 2)
        )
    }

    override fun onTouchDown() {
        CanvasService.extraCanvas.save()
        createPathObject()
        SelectionService.clearSelection()
        val numberOfLayers = DrawingObjectManager.numberOfLayers
        for (index in numberOfLayers - 1 downTo 0) {
            val drawable = DrawingObjectManager.getDrawable(index)

            // if is inside bounding box
            if (SelectionService.touchedInside(motionTouchEventX, motionTouchEventY, drawable.bounds)) {
                when(drawable) {
                    // PathShape
                    is ShapeDrawable -> {
                        var boundingBox = getPathBoundingBox(PencilService.paths[index]!!)
                        var isInsidePath = boundingBox.contains(motionTouchEventX.toInt(),motionTouchEventY.toInt())
                        if (isInsidePath) {
                            selectedShape = drawable
                            selectedShapeIndex = index
                            setSelectionBounds(boundingBox.bounds, drawable.paint.strokeWidth.toInt())
                            selectionCommand!!.execute()
                            break
                        }
                    }
                    // Ellipse and Rectangle
                    is LayerDrawable -> {
                        selectedShape = drawable
                        selectedShapeIndex = index
                        val strokeWidth = (drawable.getDrawable(1) as ShapeDrawable).paint.strokeWidth.toInt()
                        setSelectionBounds(drawable.bounds, strokeWidth)
                        selectionCommand!!.execute()
                        break
                    }
                }
            } else {
                selectionCommand!!.execute()
                SelectionService.selectedShapeIndex = -1
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
                    setSelectionBounds(boundingBox.bounds, strokeWidth)
                    translationCommand!!.execute()
                }
                is LayerDrawable -> {
                    val left = selectedShape.bounds.left + dx.toInt()
                    val top = selectedShape.bounds.top + dy.toInt()
                    val right = selectedShape.bounds.right + dx.toInt()
                    val bottom = selectedShape.bounds.bottom + dy.toInt()
                    selectedShape.setBounds(left, top, right, bottom)

                    val bounds = Rect()
                    bounds.set(left, top, right, bottom)
                    val strokeWidth = ((selectedShape as LayerDrawable).getDrawable(1) as ShapeDrawable).paint.strokeWidth.toInt()
                    setSelectionBounds(bounds, strokeWidth)
                    translationCommand!!.execute()
                }
            }
        }
    }

    override fun onTouchUp() {
//        TODO("Not yet implemented")
    }
}