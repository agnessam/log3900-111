package com.example.colorimagemobile.Shape

import android.graphics.Color
import androidx.annotation.ColorInt

class ShapeBuilder {
    var currentShapeType: ShapeType? = null
    var shapeSize = 0f
        private set

    @androidx.annotation.IntRange(from = 0, to = 255)
    var shapeOpacity = 0
        private set

    @get:ColorInt
    @ColorInt
    var shapeColor = 0
        private set

    fun withShapeType(shapeType: ShapeType?): ShapeBuilder {
        currentShapeType = shapeType
        return this
    }

    val shapeType: ShapeType?
        get() = currentShapeType

    fun withShapeSize(size: Float): ShapeBuilder {
        shapeSize = size
        return this
    }

    fun withShapeOpacity(
        @androidx.annotation.IntRange(
            from = 0,
            to = 255
        ) opacity: Int
    ): ShapeBuilder {
        shapeOpacity = opacity
        return this
    }

    fun withShapeColor(@ColorInt color: Int): ShapeBuilder {
        shapeColor = color
        return this
    }

    companion object {
        const val DEFAULT_SHAPE_SIZE = 25.0f
        const val DEFAULT_SHAPE_OPACITY = 255
        const val DEFAULT_SHAPE_COLOR = Color.BLACK
    }

    init {
        // default values
        withShapeType(ShapeType.BRUSH)
        withShapeSize(DEFAULT_SHAPE_SIZE)
        withShapeOpacity(DEFAULT_SHAPE_OPACITY)
        withShapeColor(DEFAULT_SHAPE_COLOR)
    }
}
