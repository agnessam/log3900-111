package com.example.colorimagemobile.services.drawing

import com.example.colorimagemobile.classes.toolsCommand.EllipseCommand
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.classes.toolsCommand.RectangleCommand

object TransformationManager {
    var previousTransformation: HashMap<String, String> = HashMap()

    fun saveTranslateTransformation(deltaX: Int, deltaY: Int, id: String, transformationLog: String, shapeLabel: ShapeLabel){
        val currentTransformation = " translate($deltaX $deltaY)"
        previousTransformation[id] = currentTransformation + transformationLog

        if(shapeLabel != null){
            DrawingJsonService.updateShapeTransform(previousTransformation[id]!!, id, shapeLabel)
        }
    }

    fun saveResizeTransformation(xTranslate: Float, yTranslate: Float, xScale: Float, yScale: Float, id: String, transformationLog: String, shapeLabel: ShapeLabel){
        val currentTransformation = " translate($xTranslate $yTranslate) scale($xScale $yScale) translate(-$xTranslate -$yTranslate)"
        previousTransformation[id] = currentTransformation + transformationLog

        if(shapeLabel != null){
            DrawingJsonService.updateShapeTransform(previousTransformation[id]!!, id, shapeLabel)
        }
    }
}
