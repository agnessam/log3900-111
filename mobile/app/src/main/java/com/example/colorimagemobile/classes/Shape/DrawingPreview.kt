package com.example.colorimagemobile.classes.Shape

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import com.example.colorimagemobile.Interface.BrushViewChangeListener
import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.services.drawing.CustomPaint
import com.example.colorimagemobile.services.drawing.IDGenerator
import com.example.colorimagemobile.services.drawing.PathService
import com.example.colorimagemobile.services.drawing.ToolTypeService
import com.example.colorimagemobile.services.drawing.toolsAttribute.EraserService
import java.lang.Math.abs
import java.util.*

open class DrawingPreview constructor(
    context: Context?) : View(context) {

    private var isErasing = false
    private var viewChangeListener: BrushViewChangeListener? = null
    private var points : ArrayList<Point> = arrayListOf()
    private var touchX= 0f
    private var touchY = 0f
    private var paint = Paint()

    private var currentShape: ShapeAndPaint? = ShapeAndPaint(0,BrushShape(),CustomPaint().createPaint(),arrayListOf())
    var currentShapeBuilder: ShapeBuilder? = null
    private val  drawShapes = Stack<ShapeAndPaint>()

    private val defaultEraserSize = 50.0f
    private var mBrushEraserSize: Float = defaultEraserSize

    // function use for the eraser
    fun erase(){
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        val incertitudeMargin = 50
        val totalMargin = incertitudeMargin + EraserService.currentWidth
        PathService.getPaintPath().forEach {
            // iterate over each coordinates for each shape/path
            it.points.forEach { point ->
                val xDifference = abs(point.x.toInt() - touchX.toInt()) <= totalMargin
                val yDifference = abs(point.y.toInt() - touchY.toInt()) <= totalMargin
                // remove path from list
                if (xDifference && yDifference) {
                    if (PathService.getPaintPath().size < it.id){
                        PathService.removeAll()
                        drawShapes.clear()
                    }
                    else {
                        PathService.removeByID(it.id)
                        drawShapes.clear()
                        drawShapes.addAll(PathService.getPaintPath())
                    }
                    postInvalidate()
                    return
                }
            }
        }


    }

    fun setBrushViewChangeListener(brushViewChangeListener: BrushViewChangeListener) {
        viewChangeListener = brushViewChangeListener
    }

    override fun onDraw(canvas: Canvas?) {
        for (shape in drawShapes) {
            shape!!.shape.draw(canvas!!, shape.paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (isEnabled) {
            touchX = event.x
            touchY = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    onTouchEventDown(touchX, touchY)
                }
                MotionEvent.ACTION_MOVE -> onTouchEventMove(touchX, touchY)
                MotionEvent.ACTION_UP -> onTouchEventUp(touchX, touchY)
            }
            invalidate()
            true
        } else {
            false
        }
    }

    // All tools use these functions To be use for synchronisation
    private fun onTouchEventDown(touchX: Float, touchY: Float) {
        createShape()
        if (currentShape != null && currentShape!!.shape != null) {
            currentShape!!.shape.startShape(touchX, touchY)
            points.add( Point(touchX,touchY))
        }

    }

    private fun onTouchEventMove(touchX: Float, touchY: Float) {
        if (currentShape != null && currentShape!!.shape != null) {
            currentShape!!.shape.moveShape(touchX, touchY)
        }
        points.add( Point(touchX,touchY))
    }

    private fun onTouchEventUp(touchX: Float, touchY: Float) {
        if (currentShape != null && currentShape!!.shape != null) {
            endShape(touchX, touchY)
        }
        currentShape?.let { PathService.addPaintPath(it) }
        if (isErasing) {
            erase()
        }
    }


    private fun createShape() {
        var paint = CustomPaint().createPaint()
        val shape : AbstractShape
        if (currentShapeBuilder!!.shapeType === ShapeType.ELLIPSE) {
            shape = EllipseShape()
        } else if (ToolTypeService.getCurrentToolType().value == ToolType.RECTANGLE) {
            shape = RectangleShape()
        }
        else {
            shape = BrushShape()
        }

        currentShape = ShapeAndPaint(IDGenerator.getNewId(),shape, paint, points)
        drawShapes.push(currentShape)
        if (viewChangeListener != null) {
            viewChangeListener!!.onStartDrawing()
        }
    }

    private fun endShape(touchX: Float, touchY: Float) {
        if (currentShape!!.shape.hasBeenTapped()) {

            // just a tap, this is not a shape, so remove it
            drawShapes.remove(currentShape)
        }
        if (viewChangeListener != null) {
            viewChangeListener!!.onStopDrawing()
            viewChangeListener!!.onViewAdd(this)
        }
    }

    fun setBrushEraserSize(brushEraserSize: Float) {
        mBrushEraserSize = brushEraserSize
    }

    fun setShapeBuilder(shapeBuilder: ShapeBuilder) {
        currentShapeBuilder = shapeBuilder
    }

    fun enableDrawing(brushDrawMode: Boolean) {
        isEnabled = brushDrawMode
        isErasing = !brushDrawMode
        if (brushDrawMode) {
            visibility = VISIBLE
        }
    }


    private fun setupBrushDrawing() {
        //Caution: This line is to disable hardware acceleration to make eraser feature work properly
        setLayerType(LAYER_TYPE_HARDWARE, null)
        visibility = GONE
        currentShapeBuilder = ShapeBuilder()
    }

    init {
        setupBrushDrawing()
    }

}
