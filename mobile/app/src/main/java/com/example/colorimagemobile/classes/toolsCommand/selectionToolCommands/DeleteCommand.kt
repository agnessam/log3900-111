package com.example.colorimagemobile.classes.toolsCommand

import android.graphics.drawable.Drawable
import com.example.colorimagemobile.interfaces.ICommand

class DeleteCommand(deletedShape: Drawable): ICommand {
    private var deletedShape = deletedShape

    override fun update(drawingCommand: Any) {
        TODO("Not yet implemented")
    }

    override fun execute() {
        TODO("Not yet implemented")
    }
}