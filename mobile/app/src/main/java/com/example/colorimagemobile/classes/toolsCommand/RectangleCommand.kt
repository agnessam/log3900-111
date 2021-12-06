package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.RectangleData
import com.example.colorimagemobile.models.RectangleUpdate
import com.example.colorimagemobile.services.drawing.*
import com.example.colorimagemobile.services.drawing.Point
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import kotlin.math.min

class RectangleCommand(rectangleData: RectangleData): ICommand {
    private var boundingRectangle = Rect(0,0, CanvasService.extraCanvas.width, CanvasService.extraCanvas.height)

    private var startingPoint: Point? = null
    var endingPoint: Point? = null

    private var layerIndex: Int = -1
    private var fillRectangleIndex: Int = -1
    var borderRectangleIndex: Int = -1

    var rectangle: RectangleData = rectangleData

    private lateinit var rectangleShape: LayerDrawable

    var borderPaint: Paint = Paint()
    var fillPaint: Paint = Paint()

    var borderPath = Path()
    var fillPath = Path()
    init{
        var fillRectangle = createNewRectangle()
        var borderRectangle = createNewRectangle()

        initializeRectangleLayers(fillRectangle, borderRectangle)

        borderPaint = initializePaint(rectangleData.stroke, rectangleData.strokeOpacity, Color.WHITE)
        fillPaint = initializePaint(rectangleData.fill,  rectangleData.fillOpacity, Color.BLACK)

        setStartPoint(Point(rectangle.x.toFloat(), rectangle.y.toFloat()))
        DrawingJsonService.createRect(rectangle)
    }

    fun initializePaint(color: String, opacity: String, defaultColor: Int): Paint{
        var paint = Paint()

        val transformedColor = ColorService.addAlphaToRGBA(color, opacity)
        paint.color = if(color != "none") ColorService.rgbaToInt(transformedColor) else defaultColor
        paint.alpha = ColorService.convertOpacityToAndroid(opacity)
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.isDither = true
        return paint
    }

    private fun initializeRectangleLayers(fillRectangle: ShapeDrawable, borderRectangle: ShapeDrawable) {
        var rectangleShapeArray = arrayOf<Drawable>()
        rectangleShape = LayerDrawable(rectangleShapeArray)

        fillRectangleIndex = rectangleShape.addLayer(fillRectangle)
        borderRectangleIndex = rectangleShape.addLayer(borderRectangle)
        layerIndex = DrawingObjectManager.addLayer(rectangleShape, rectangle.id)
    }

    private fun createNewRectangle(): ShapeDrawable{
        val pathShape = PathShape(Path(),
            CanvasService.extraCanvas.width.toFloat(), CanvasService.extraCanvas.height.toFloat())
        return ShapeDrawable(pathShape)
    }

    fun setStartPoint(startPoint: Point) {
        startingPoint = startPoint
    }

    fun setEndPoint(endPoint: Point) {
        endingPoint = endPoint

        rectangle.width = kotlin.math.abs(endingPoint!!.x - startingPoint!!.x)
        rectangle.x = min(endingPoint!!.x, startingPoint!!.x)
        rectangle.height = kotlin.math.abs(endingPoint!!.y - startingPoint!!.y)
        rectangle.y = min(endingPoint!!.y, startingPoint!!.y)

        DrawingJsonService.updateRect(rectangle)

        this.generateBorderPath()
        this.generateFillPath()
    }

    private fun getFillRectangle(): ShapeDrawable{
        return rectangleShape.getDrawable(fillRectangleIndex) as ShapeDrawable
    }

    private fun getBorderRectangle(): ShapeDrawable{
        return rectangleShape.getDrawable(borderRectangleIndex) as ShapeDrawable
    }

    private fun getRectangleDrawable(): LayerDrawable?{
        val drawable = DrawingObjectManager.getDrawable(this.layerIndex) ?: return null
        return drawable as LayerDrawable
    }

    override fun update(drawingCommand: Any) {
        if(drawingCommand is RectangleUpdate){
            rectangle.x = drawingCommand.x
            rectangle.y = drawingCommand.y
            rectangle.width = drawingCommand.width
            rectangle.height = drawingCommand.height
            generateFillPath()
            generateBorderPath()
        }
    }

    override fun execute() {
        if(this.getRectangleDrawable() == null) return

        if(rectangle.stroke != "none"){
            val borderRectPathShape = PathShape(borderPath,
                CanvasService.extraCanvas.width.toFloat(), CanvasService.extraCanvas.height.toFloat()
            )
            var borderRectDrawable = ShapeDrawable(borderRectPathShape)
            this.getBorderRectangle().bounds = this.boundingRectangle

            this.getRectangleDrawable()!!.setDrawable(this.borderRectangleIndex, borderRectDrawable)
            this.getBorderRectangle().paint.set(this.borderPaint)
        }

        if(rectangle.fill != "none"){
            val fillRectPathShape = PathShape(fillPath,
                CanvasService.extraCanvas.width.toFloat(), CanvasService.extraCanvas.height.toFloat()
            )

            var fillRectDrawable = ShapeDrawable(fillRectPathShape)
            this.getFillRectangle().bounds = this.boundingRectangle

            this.getRectangleDrawable()!!.setDrawable(this.fillRectangleIndex, fillRectDrawable)
            this.getFillRectangle().paint.set(this.fillPaint)
        }

        this.getRectangleDrawable()!!.bounds = this.boundingRectangle
        DrawingObjectManager.addCommand(rectangle.id, this)
        DrawingObjectManager.setDrawable(layerIndex, rectangleShape)
        CanvasUpdateService.invalidate()
    }

    private fun generateFillPath(){
        var left = rectangle.x
        var top = rectangle.y

        var right = rectangle.x + rectangle.width
        var bottom = rectangle.y + rectangle.height
        traceFillPath(left, top, right, bottom)
    }

    private fun modifyBounds(left: Float, top: Float, right: Float, bottom: Float): RectF{
        var borderModifier = if(rectangle.stroke == "none") 0f else rectangle.strokeWidth.toFloat()

        var rect = RectF(left, top, right, bottom)
        rect.left += borderModifier
        rect.top += borderModifier

        rect.right -= borderModifier
        rect.bottom -= borderModifier
        if(rect.right < rect.left) rect.right = rect.left
        if(rect.bottom < rect.top) rect.bottom = rect.top
        return rect
    }

    fun traceFillPath(left: Float, top: Float, right: Float, bottom: Float){
        var rect = modifyBounds(left, top, right, bottom)
        fillPath = Path()
        fillPath.addRect(rect, Path.Direction.CW)
    }

    private fun generateBorderPath(){
        var left = rectangle.x
        var top = rectangle.y
        var right = rectangle.x + rectangle.width
        var bottom = rectangle.y + rectangle.height

        traceBorderPath(left, top, right, bottom)
    }

    fun traceBorderPath(left: Float, top: Float, right: Float, bottom: Float){
        var rect = RectF(left, top, right, bottom)
        borderPath = Path()

        borderPath.addRect(rect, Path.Direction.CW)
        borderPath.close()

        val innerRect = RectF(rect)
        innerRect.inset(this.rectangle.strokeWidth.toFloat(), this.rectangle.strokeWidth.toFloat())
        if (innerRect.width() > 0 && innerRect.height() > 0) {
            borderPath.addRect(innerRect, Path.Direction.CW)
            borderPath.close()
        }

        borderPath.fillType = Path.FillType.EVEN_ODD
    }
}