package com.example.colorimagemobile.classes.openDrawingCommand

import com.example.colorimagemobile.classes.toolsCommand.TranslateCommand
import com.example.colorimagemobile.classes.toolsCommand.selectionToolCommands.ResizeCommand
import com.example.colorimagemobile.models.SvgStyle
import com.example.colorimagemobile.models.ToolData
import com.example.colorimagemobile.models.TranslateData

interface ICreateDrawingCommand {
    fun createData(style: SvgStyle): ToolData
    fun execute()


    fun transformShape(transformString: String, objectId: String) {
        var transformStrings = parseTransformationString(transformString)

        while (transformStrings.count() > 0){
            if(transformStrings.count() >= 3){
                var endIndex = 0
                endIndex = if("scale" in transformStrings[transformStrings.count() - 2]){
                    val translateString = transformStrings[transformStrings.count() - 3]
                    val scaleString = transformStrings[transformStrings.count() - 2]
                    var resizeCommand = parseResizeCommand(translateString, scaleString, objectId)
                    resizeCommand?.execute()

                    transformStrings.count() - 4
                } else{
                    val translateString = transformStrings[transformStrings.count() - 1]
                    executeTranslation(translateString, objectId)

                    transformStrings.count() - 2
                }
                transformStrings = ArrayList(transformStrings.slice(IntRange(0, endIndex)))
            }
            else {
                for(string in transformStrings){
                    executeTranslation(string, objectId)
                }
                break
            }
        }
    }

    private fun executeTranslation(translateString: String, objectId: String){
        var translateCommand = parseTranslateCommand(translateString, objectId)
        translateCommand.execute()
    }

    private fun parseTransformationString(str: String): ArrayList<String>{
        var transformArray = str.trim().split(" ")
        var newArray = ArrayList<String>()
        var i = 0
        while( i < transformArray.count() - 1){
            var transformString = transformArray[i] + " " + transformArray[i + 1]
            newArray.add(transformString)
            i += 2
        }
        return newArray
    }

    private fun parseTranslateCommand(transformString: String, objectId: String):TranslateCommand {
        val (xTranslate, yTranslate) = getXAndYTranslate(transformString)
        var translateData = TranslateData(objectId, xTranslate, yTranslate)
        var translateCommand = TranslateCommand(translateData)
        translateCommand.setTransformation(xTranslate, yTranslate)
        return translateCommand
    }

    private fun parseResizeCommand(translateString: String, scaleString: String, objectId: String): ResizeCommand?{
        val (xTranslate, yTranslate) = getXAndYTranslate(translateString)
        val (xScale, yScale) = getXAndYScale(scaleString)
        var resizeCommand = ResizeCommand(objectId)
        resizeCommand.setScales(xScale, yScale, xTranslate, yTranslate)
        return resizeCommand
    }

    private fun getXAndYScale(str: String): Pair<Float, Float> {
        return getXAndYAttributes(str, "scale")
    }

    private fun getXAndYTranslate(str: String): Pair<Float, Float>{
        return getXAndYAttributes(str, "translate")
    }

    private fun getXAndYAttributes(transformString: String, prefix:String): Pair<Float, Float>{
        var translation = transformString.trim().replace("$prefix(", "").replace(")", "").split(" ")
        var xTranslate = translation[0].toFloat()
        var yTranslate = translation[1].toFloat()

        return Pair(xTranslate, yTranslate)
    }
}