package com.example.colorimagemobile.classes.commands

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.R
import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.interfaces.IToolCommand
import com.example.colorimagemobile.ui.home.fragments.drawing.attributes.eraser.EraserFragment
import com.example.colorimagemobile.ui.home.fragments.drawing.views.CanvasView
import com.example.colorimagemobile.ui.home.fragments.drawing.views.EraserView

class EraserCommand: IToolCommand {
    override fun getType(): ToolType {
        return ToolType.ERASER
    }

    override fun getIcon(): Int {
        return R.drawable.drawing_icon_eraser
    }

    override fun getView(context: Context): CanvasView {
        return EraserView(context)
    }

    override fun getFragment(): Fragment {
        return EraserFragment()
    }
}