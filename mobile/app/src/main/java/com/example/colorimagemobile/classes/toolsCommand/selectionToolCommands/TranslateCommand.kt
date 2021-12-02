package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.Matrix
import android.graphics.Path
import com.example.colorimagemobile.interfaces.ICommand
import com.example.colorimagemobile.models.SyncUpdate
import com.example.colorimagemobile.models.TranslateData
import com.example.colorimagemobile.services.drawing.*

class TranslateCommand(translateData: TranslateData): ICommand {

    companion object{
        private var objectCurrentTranslate: HashMap<String, Array<Float>> = HashMap()
    }

    private var deltaX : Float
    private var deltaY: Float

    private var commandToTranslate: ICommand? = null

    private var transformationLog: String = ""
    private var id: String

    private var resizeFillPath: Path? = null
    private var resizeBorderPath: Path? = null

    private val shapeLabel: ShapeLabel?

    override fun update(drawingCommand: Any) {
        setTransformation((drawingCommand as TranslateData).deltaX, drawingCommand.deltaY)
    }

    init{
        commandToTranslate = DrawingObjectManager.getCommand(translateData.id)
        resetPathWithShapePath()
        shapeLabel = when(commandToTranslate){
            is PencilCommand -> ShapeLabel.POLYLINE
            is RectangleCommand -> ShapeLabel.RECTANGLE
            is EllipseCommand -> ShapeLabel.ELLIPSE
            else -> null
        }

        id = translateData.id

        // Initialize deltaX and deltaY
        if(!objectCurrentTranslate.containsKey(id) || objectCurrentTranslate[id] == null ){
            objectCurrentTranslate[id] = arrayOf(0f, 0f)
        }
        deltaX = objectCurrentTranslate[id]!![0]
        deltaY = objectCurrentTranslate[id]!![1]

        if(!TransformationManager.previousTransformation.containsKey(id)){
            TransformationManager.previousTransformation[id] = ""
        }
        transformationLog = TransformationManager.previousTransformation[id]!!
    }

    private fun resetPathWithShapePath() {
        when(commandToTranslate) {
            is PencilCommand -> (commandToTranslate as PencilCommand).getPath()
            is EllipseCommand -> (commandToTranslate as EllipseCommand).getPaths()
            is RectangleCommand -> (commandToTranslate as RectangleCommand).getPaths()
            else -> null
        }
    }

    private fun PencilCommand.translate() {
        path = Path(resizeBorderPath!!)
        val translationMatrix = Matrix()
        translationMatrix.setTranslate(deltaX.toFloat(), deltaY.toFloat())
        path.transform(translationMatrix)
        execute()
    }

    private fun RectangleCommand.translate() {
        fillPath = Path(resizeFillPath!!)
        borderPath = Path(resizeBorderPath!!)
        val translationMatrix = Matrix()
        translationMatrix.setTranslate(deltaX.toFloat(), deltaY.toFloat())
        borderPath.transform(translationMatrix)
        fillPath.transform(translationMatrix)
        execute()
    }

    private fun EllipseCommand.translate() {
        fillPath = Path(resizeFillPath!!)
        borderPath = Path(resizeBorderPath!!)
        val translationMatrix = Matrix()
        translationMatrix.setTranslate(deltaX.toFloat(), deltaY.toFloat())
        borderPath.transform(translationMatrix)
        fillPath.transform(translationMatrix)
        execute()
    }

    fun setTransformation(x: Float, y: Float) {
        this.deltaX = x + objectCurrentTranslate[id]!![0]
        this.deltaY = y + objectCurrentTranslate[id]!![1]
    }

    override fun execute() {
        when(commandToTranslate){
            is PencilCommand -> (commandToTranslate as PencilCommand).translate()
            is RectangleCommand -> (commandToTranslate as RectangleCommand).translate()
            is EllipseCommand -> (commandToTranslate as EllipseCommand).translate()
            else -> {}
        }
        objectCurrentTranslate[id] = arrayOf(deltaX, deltaY)
        CanvasUpdateService.invalidate()
        if(shapeLabel != null){
            TransformationManager.saveTranslateTransformation(deltaX, deltaY, id, transformationLog, shapeLabel)
        }
    }

    private fun PencilCommand.getPath(){
        resizeBorderPath = path
    }

    private fun EllipseCommand.getPaths(){
        resizeFillPath = fillPath
        resizeBorderPath = borderPath
    }

    private fun RectangleCommand.getPaths(){
        resizeFillPath = fillPath
        resizeBorderPath = borderPath
    }

}