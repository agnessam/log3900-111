package com.example.colorimagemobile.services.drawing.toolsAttribute

object RectangleService : Attributes {
    override val minWidth = 1
    override val maxWidth = 40
    override var currentWidth: Int = 0

    init {
        RectangleService.initCurrentWidth()
    }
}