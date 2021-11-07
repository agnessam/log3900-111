package com.example.colorimagemobile.Shape

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.colorimagemobile.Interface.BrushViewChangeListener
import com.example.colorimagemobile.services.drawing.IDGenerator
import com.example.colorimagemobile.services.drawing.PathService
import com.example.colorimagemobile.services.drawing.toolsAttribute.EraserService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import java.lang.Math.abs
import java.util.*

class DrawingView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    View(context, attrs, defStyle) {

    private var isErasing = false
    private val DEFAULT_ERASER_SIZE = 50.0f
    private var mBrushEraserSize: Float = DEFAULT_ERASER_SIZE
    private var viewChangeListener: BrushViewChangeListener? = null
    private var points : ArrayList<Point> = arrayListOf()
    private var touchX= 0f
    private var touchY = 0f
    private var paint = Paint()

    private var currentShape: ShapeAndPaint? = null

    var currentShapeBuilder: ShapeBuilder? = null
    private val  drawShapes = Stack<ShapeAndPaint>()

    private fun createPaint(): Paint {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)

        // apply shape builder parameters
        paint.strokeWidth = currentShapeBuilder!!.shapeSize
        paint.alpha = currentShapeBuilder!!.shapeOpacity
        paint.color = currentShapeBuilder!!.shapeColor
        return paint
    }

    // function use for the eraser
    fun erase(){
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        val incertitudeMargin = 50
        val totalMargin = incertitudeMargin + EraserService.currentWidth
        printMsg("in delete before boucle condition 1: = "+points)
        PathService.getPaintPath().forEach {
            printMsg("inside foreach to delete points premier path : = "+PathService.getPaintPath().toString())
            // iterate over each coordinates for each shape/path
            it.points.forEach { point ->
                printMsg("inside foreach to delete points  for each id : = "+it.id.toString())
                val xDifference = abs(point.x.toInt() - touchX.toInt()) <= totalMargin
                val yDifference = abs(point.y.toInt() - touchY.toInt()) <= totalMargin
                printMsg("xDifference && yDifference : = "+xDifference +" "+yDifference)
                // remove path from list
                if (xDifference && yDifference) {
                    if (PathService.getPaintPath().size < it.id){
                        PathService.removeAll()
                        drawShapes.clear()
                    }
                    else {
                        PathService.removeByID(it.id)
                        printMsg("in delete function remove id : = "+it.id)
                        printMsg("SIZEBEFORE DELETEEEEE = "+drawShapes.size)
                        drawShapes.clear()
                        drawShapes.addAll(PathService.getPaintPath())

                    }
                    postInvalidate()
                    printMsg("SIZENOWWWWWWWWW = "+drawShapes.size)


                    printMsg("New path after defete func : = "+drawShapes)
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

    private fun onTouchEventDown(touchX: Float, touchY: Float) {
        createShape()
        if (currentShape != null && currentShape!!.shape != null) {
            currentShape!!.shape.startShape(touchX, touchY)
            points.add( Point
                (touchX,
                touchY)
                    )
        }

    }

    private fun onTouchEventMove(touchX: Float, touchY: Float) {
        if (currentShape != null && currentShape!!.shape != null) {
            currentShape!!.shape.moveShape(touchX, touchY)
        }
        points.add( Point
            (touchX,
            touchY)
        )
    }

    private fun onTouchEventUp(touchX: Float, touchY: Float) {
        if (currentShape != null && currentShape!!.shape != null) {
            currentShape!!.shape.stopShape()
            endShape(touchX, touchY)


        }
        currentShape?.let { PathService.addPaintPath(it) }

        //Check the path for pathservice and drawshape for erase test
        printMsg("allpath: = "+PathService.getPaintPath())
        printMsg("allpath dans drawshapes: = "+drawShapes)

        if (isErasing) {
            //check if erase is call
            printMsg("event up erase")
            erase()
        }
    }


    private fun createShape() {
        var paint = createPaint()
        val shape : AbstractShape
        if (currentShapeBuilder!!.shapeType === ShapeType.ELLIPSE) {
            shape = EllipseShape()
        } else if (currentShapeBuilder!!.shapeType === ShapeType.RECTANGLE) {
            shape = RectangleShape()
        } else if (currentShapeBuilder!!.shapeType === ShapeType.LINE) {
            shape = LineShape()
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

    fun brushEraser() {
        isEnabled = true
        isErasing = true
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
