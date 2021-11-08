package com.example.colorimagemobile.classes.Shape

import android.graphics.Path
import android.util.Log


class RectangleShape : AbstractShape() {

    private var lastX = 0f
    private var lastY = 0f
    override val tag: String

        protected get() = "RectangleShape"

    override fun startShape(x: Float, y: Float) {
        Log.d(tag, "startShape@ $x,$y")
        left = x
        top = y
    }

    override fun moveShape(x: Float, y: Float) {
        right = x
        bottom = y
        val dx = Math.abs(x - lastX)
        val dy = Math.abs(y - lastY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path = createRectanglePath()
            lastX = x
            lastY = y
        }
    }

    private fun createRectanglePath(): Path {
        val path = Path()
        path.moveTo(left, top)
        path.lineTo(left, bottom)
        path.lineTo(right, bottom)
        path.lineTo(right, top)
        path.close()
        return path
    }

}
