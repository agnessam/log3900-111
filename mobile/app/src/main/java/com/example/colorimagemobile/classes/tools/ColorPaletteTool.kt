package com.example.colorimagemobile.classes.tools

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.R
import com.example.colorimagemobile.Shape.DrawingPreview
import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.interfaces.ITool
import com.example.colorimagemobile.ui.home.fragments.drawing.attributes.colorPalette.ColorPaletteFragment

class ColorPaletteTool: ITool {
    override fun getTitle(): String {
        return "Color Palette"
    }

    override fun getType(): ToolType {
        return ToolType.COLOR_PALETTE
    }

    override fun getIcon(): Int {
        return R.drawable.drawing_icon_palette
    }

    override fun getView(context: Context): DrawingPreview? {
        return null
    }

    override fun getFragment(): Fragment {
        return ColorPaletteFragment()
    }
}