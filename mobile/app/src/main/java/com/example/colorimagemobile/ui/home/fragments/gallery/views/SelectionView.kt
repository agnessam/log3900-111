package com.example.colorimagemobile.ui.home.fragments.gallery.views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import android.graphics.drawable.shapes.RectShape
import android.text.Selection
import com.example.colorimagemobile.classes.toolsCommand.ResizeCommand
import com.example.colorimagemobile.classes.toolsCommand.SelectionCommand
import com.example.colorimagemobile.models.SelectionData
import com.example.colorimagemobile.services.UUIDService
import com.example.colorimagemobile.services.drawing.AnchorIndexes
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.SelectionService
import com.example.colorimagemobile.services.drawing.SelectionService.inBounds
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import com.example.colorimagemobile.services.drawing.toolsAttribute.ResizeSelectionService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import kotlin.io.path.Path

class SelectionView(context: Context?): CanvasView(context) {
    private var selectionCommand: SelectionCommand? = null

    override fun createPathObject() {
        selectionCommand = SelectionCommand()

        // for sync
        val id = UUIDService.generateUUID()
        var selectionData = SelectionData(
            id = id,
        )
    }

    private fun getPathBoundingBox(path: Path, strokeWidth: Float): Region {
        val rectF = RectF()
        path.computeBounds(rectF, true)
        var region = Region()
        region.setPath(
            path,
            Region(
                rectF.left.toInt() - strokeWidth.toInt(),
                rectF.top.toInt() - strokeWidth.toInt(),
                rectF.right.toInt() + strokeWidth.toInt(),
                rectF.bottom.toInt() + strokeWidth.toInt()
            )
        )

        return region
    }



    override fun onTouchDown() {
        CanvasService.extraCanvas.save()
        createPathObject()
        val numberOfLayers = CanvasService.layerDrawable.numberOfLayers
        for (index in numberOfLayers - 1 downTo 0) {
            val drawable = CanvasService.layerDrawable.getDrawable(index)
            // PathDrawables have bounds equal to the dimensions of the canvas and
            // click will always be inside of them

            // if is inside bounding box
            if(SelectionService.selectedDrawable == drawable && SelectionService.getSelectedAnchor(motionTouchEventX, motionTouchEventY) != null){
                SelectionService.setSelectedAnchor(motionTouchEventX, motionTouchEventY)
                break
            }
            if (inBounds(motionTouchEventX, motionTouchEventY, drawable.bounds)) {
//                selectionCommand?.execute()
                if(SelectionService.selectedDrawable == drawable) break

                    SelectionService.selectedDrawable = drawable
                    when(drawable) {
                        // PathShape
                        is ShapeDrawable -> {
                            var isInsidePath = false
                            var boundingBox = Region()
                            PencilService.paths[index]?.let { path ->
                                boundingBox = getPathBoundingBox(path, drawable.paint.strokeWidth)
                                isInsidePath = boundingBox.contains(motionTouchEventX.toInt(),motionTouchEventY.toInt())
                            }
                            if (isInsidePath) {
                                SelectionService.setSelectionBounds(
                                    boundingBox.bounds.left,
                                    boundingBox.bounds.top,
                                    boundingBox.bounds.right,
                                    boundingBox.bounds.bottom
                                )
                                selectionCommand!!.execute()
                                break
                            }
                        }
                        // Ellipse and Rectangle
                        is LayerDrawable -> {
                            SelectionService.setSelectionBounds(
                                drawable.bounds.left,
                                drawable.bounds.top,
                                drawable.bounds.right,
                                drawable.bounds.bottom)
                            selectionCommand!!.execute()
                            break
                        }

                }
            } else {
                SelectionService.clearSelection()
                selectionCommand!!.execute()
            }
        }
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    override fun onTouchMove() {
        // Translation: touching inside bounding box
        // Resizing: touching one of 8 points on bounding box
        if(SelectionService.selectedAnchorIndex != AnchorIndexes.NONE){
            if(SelectionService.selectedDrawable != null){
                if(ResizeSelectionService.resizeCommand == null) {
                    ResizeSelectionService.resizeCommand = ResizeSelectionService.createResizeCommand(SelectionService.selectedDrawable!!)
                }
                var offSet = SelectionService.getBoundsSelection()
                ResizeSelectionService.resize(motionTouchEventX, motionTouchEventY, offSet)
            }
        }
    }

    override fun onTouchUp() {
//        TODO("Not yet implemented")
        SelectionService.resetSelectedAnchor()
        ResizeSelectionService.resizeCommand = null
    }
}