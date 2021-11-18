package com.example.colorimagemobile.services.drawing

import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RectShape
import com.example.colorimagemobile.classes.toolsCommand.ResizeCommand
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

// Helper to draw selection box to canvas
object SelectionService {
    lateinit var selectedShape: Drawable
    var selectedShapeIndex: Int = -1

    var selectionBox: LayerDrawable = LayerDrawable(arrayOf<Drawable>())
    private lateinit var selectionRectangle: ShapeDrawable
    var selectedDrawable: Drawable? = null
    var selectedAnchorIndex: AnchorIndexes = AnchorIndexes.NONE

    private lateinit var topCtrl: LayerDrawable
    private lateinit var leftCtrl: LayerDrawable
    private lateinit var rightCtrl: LayerDrawable
    private lateinit var bottomCtrl: LayerDrawable
    private lateinit var topLeftCtrl: LayerDrawable
    private lateinit var topRightCtrl: LayerDrawable
    private lateinit var bottomLeftCtrl: LayerDrawable
    private lateinit var bottomRightCtrl: LayerDrawable
    var anchors: HashMap<LayerDrawable, AnchorIndexes> = HashMap()

    private val pointWidth: Int = 5

    private var borderPaint = Paint()
    private var fillPaint = Paint()

    fun initSelectionRectangle() {
        var paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.MITER
        paint.strokeWidth = 1F
        paint.color = Color.BLACK
        paint.alpha = 255
        var intervals = arrayOf(10F, 10F)
        paint.pathEffect = DashPathEffect(intervals.toFloatArray(), 0F)

        selectionRectangle = ShapeDrawable(RectShape())
        selectionRectangle.paint.set(paint)

        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeJoin = Paint.Join.MITER
        borderPaint.color = Color.BLACK
        borderPaint.strokeWidth = 1F
        borderPaint.alpha = 255

        fillPaint.style = Paint.Style.FILL
        fillPaint.strokeJoin = Paint.Join.MITER
        fillPaint.color = Color.WHITE
        fillPaint.strokeWidth = 1F
        fillPaint.alpha = 255
    }

    fun setSelectionBounds(left: Int, top: Int, right: Int, bottom: Int) {
        var selectionBounds = Rect()
        selectionBounds.set(left, top, right, bottom)

        selectionRectangle.bounds = selectionBounds
        selectionBox.addLayer(selectionRectangle)

        var width = right - left
        var height = bottom - top
        var horizontalCenter = left + width / 2
        var verticalCenter = top + height / 2

        topCtrl = setResizingPoint(horizontalCenter, top)
        leftCtrl = setResizingPoint(left, verticalCenter)
        rightCtrl = setResizingPoint(right, verticalCenter)
        bottomCtrl = setResizingPoint(horizontalCenter, bottom)
        topLeftCtrl = setResizingPoint(left, top)
        topRightCtrl = setResizingPoint(right, top)
        bottomLeftCtrl = setResizingPoint(left, bottom)
        bottomRightCtrl = setResizingPoint(right, bottom)

        selectionBox.addLayer(topCtrl)
        selectionBox.addLayer(leftCtrl)
        selectionBox.addLayer(rightCtrl)
        selectionBox.addLayer(bottomCtrl)
        selectionBox.addLayer(topLeftCtrl)
        selectionBox.addLayer(topRightCtrl)
        selectionBox.addLayer(bottomLeftCtrl)
        selectionBox.addLayer(bottomRightCtrl)


        anchors[topLeftCtrl] = AnchorIndexes.TOP_LEFT
        anchors[topCtrl] = AnchorIndexes.TOP
        anchors[topRightCtrl] = AnchorIndexes.TOP_RIGHT
        anchors[leftCtrl] = AnchorIndexes.LEFT
        anchors[rightCtrl] = AnchorIndexes.RIGHT
        anchors[bottomLeftCtrl] = AnchorIndexes.BOTTOM_LEFT
        anchors[bottomCtrl] = AnchorIndexes.BOTTOM
        anchors[bottomRightCtrl] = AnchorIndexes.BOTTOM_RIGHT
    }

    private fun setResizingPoint(x: Int, y: Int): LayerDrawable {
        var borderShape = ShapeDrawable(RectShape())
        var fillShape = ShapeDrawable(RectShape())

        borderShape.paint.set(borderPaint)
        fillShape.paint.set(fillPaint)

        borderShape.setBounds(
            x - pointWidth,
            y - pointWidth,
            x + pointWidth,
            y + pointWidth
        )
        fillShape.setBounds(
            x - pointWidth,
            y - pointWidth,
            x + pointWidth,
            y + pointWidth
        )

        var pointShape = LayerDrawable(arrayOf<Drawable>())
        pointShape.addLayer(fillShape)
        pointShape.addLayer(borderShape)
        pointShape.setBounds(
            x - pointWidth,
            y - pointWidth,
            x + pointWidth,
            y + pointWidth)

        return pointShape
    }

    fun getSelectedAnchor(motionTouchEventX: Float, motionTouchEventY: Float): AnchorIndexes {
        for (anchor in anchors.keys) {
            // PathDrawables have bounds equal to the dimensions of the canvas and
            // click will always be inside of them

            // if is inside bounding box
            if (inBounds(motionTouchEventX, motionTouchEventY, anchor.bounds)) {
                return anchors[anchor]!!
            }
        }
        return AnchorIndexes.NONE
    }

    fun setSelectedAnchor(motionTouchEventX: Float, motionTouchEventY: Float) {
        for (anchor in anchors.keys) {
            // PathDrawables have bounds equal to the dimensions of the canvas and
            // click will always be inside of them

            // if is inside bounding box
            if (motionTouchEventX <= anchor.bounds.right &&
                motionTouchEventX >= anchor.bounds.left &&
                motionTouchEventY >= anchor.bounds.top &&
                motionTouchEventY <= anchor.bounds.bottom) {
                    selectedAnchorIndex = if(anchors[anchor] == null) AnchorIndexes.NONE else anchors[anchor]!!
                    return;
            }
        }
        selectedAnchorIndex = AnchorIndexes.NONE
    }

    fun clearSelection() {
        selectionBox = LayerDrawable(arrayOf<Drawable>())
        anchors = HashMap()
        selectedDrawable = null
    }

    fun inBounds(motionTouchEventX: Float, motionTouchEventY: Float, bounds: Rect): Boolean{
        return motionTouchEventX <= bounds.right &&
                motionTouchEventX >= bounds.left &&
                motionTouchEventY >= bounds.top &&
                motionTouchEventY <= bounds.bottom
    }

    fun resetSelectedAnchor() {
        selectedAnchorIndex = AnchorIndexes.NONE
    }

    fun getBoundsSelection(): Rect{
        var left = (topLeftCtrl.bounds.left + topLeftCtrl.bounds.right) / 2
        var top = (topLeftCtrl.bounds.top + topLeftCtrl.bounds.bottom) / 2
        var right = (bottomRightCtrl.bounds.left + bottomRightCtrl.bounds.right) / 2
        var bottom = (bottomRightCtrl.bounds.top + bottomRightCtrl.bounds.bottom) / 2
        return Rect(left, top, right, bottom)
    }

    fun getPathBoundingBox(path: Path): Region {
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


}

enum class AnchorIndexes {
    NONE,
    TOP_LEFT,
    TOP,
    TOP_RIGHT,
    LEFT,
    RIGHT,
    BOTTOM_LEFT,
    BOTTOM,
    BOTTOM_RIGHT
}