package com.example.colorimagemobile.classes.tools

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.Shape.DrawingPreview
import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.Interface.ITool
import com.example.colorimagemobile.ui.home.fragments.drawing.attributes.pencil.PencilFragment

class PencilTool: ITool {
    override fun getTitle(): String {
        return "Pencil"
    }

    override fun getType(): ToolType {
        return ToolType.PENCIL
    }

    override fun getIcon(): Int {
        return R.drawable.drawing_icon_pencil
    }

    override fun getView(context: Context): DrawingPreview {
        return DrawingPreview(context)
    }

    override fun getFragment(): Fragment {
        return PencilFragment()
    }
}