package com.example.colorimagemobile.classes.commands

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.R
import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.interfaces.IToolCommand
import com.example.colorimagemobile.ui.home.fragments.drawing.attributes.pencil.PencilFragment
import com.example.colorimagemobile.ui.home.fragments.drawing.views.CanvasView
import com.example.colorimagemobile.ui.home.fragments.drawing.views.PencilView

class PencilCommand: IToolCommand {
    override fun getType(): ToolType {
        return ToolType.PENCIL
    }

    override fun getIcon(): Int {
        return R.drawable.drawing_icon_pencil
    }

    override fun getView(context: Context): CanvasView {
        return PencilView(context)
    }

    override fun getFragment(): Fragment {
        return PencilFragment()
    }
}