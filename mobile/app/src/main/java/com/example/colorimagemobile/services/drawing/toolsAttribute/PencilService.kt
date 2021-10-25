package com.example.colorimagemobile.services.drawing.toolsAttribute

object PencilService: Attributes {
    override val minWidth = 1
    override val maxWidth = 40
    override var currentWidth: Int = 0

    init {
        initCurrentWidth()
    }
}