package com.example.colorimagemobile.Shape

import android.graphics.Path
import android.util.Log


class LineShape : AbstractShape() {
    private var lastX = 0f
    private var lastY = 0f
    protected override val tag: String
        protected get() = "LineShape"

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
            path = createLinePath()
            lastX = x
            lastY = y
        }
    }

    private fun createLinePath(): Path {
        val path = Path()
        path.moveTo(left, top)
        path.lineTo(right, bottom)
        path.close()
        return path
    }

    override fun stopShape() {
        Log.d(tag, "stopShape")
    }
}