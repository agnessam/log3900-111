package com.example.colorimagemobile.classes.commands

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.R
import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.interfaces.IToolCommand
import com.example.colorimagemobile.ui.home.fragments.drawing.attributes.colorPalette.ColorPaletteFragment
import com.example.colorimagemobile.ui.home.fragments.drawing.views.CanvasView

class ColorPaletteCommand: IToolCommand {
    override fun getType(): ToolType {
        return ToolType.COLOR_PALETTE
    }

    override fun getIcon(): Int {
        return R.drawable.drawing_icon_palette
    }

    override fun getView(context: Context): CanvasView? {
        return null
    }

    override fun getFragment(): Fragment {
        return ColorPaletteFragment()
    }
}