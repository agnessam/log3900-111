package com.example.colorimagemobile.classes.tools

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.R
import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.interfaces.ITool
import com.example.colorimagemobile.ui.home.fragments.drawing.attributes.rectangle.RectangleFragment
import com.example.colorimagemobile.ui.home.fragments.drawing.views.CanvasView
import com.example.colorimagemobile.ui.home.fragments.drawing.views.PencilView

class RectangleTool : ITool {
    override fun getTitle(): String {
        return "rectangle"
    }

    override fun getType(): ToolType {
        return ToolType.RECTANGLE
    }

    override fun getIcon(): Int {
        return R.drawable.rectangle
    }

    override fun getView(context: Context): CanvasView {
        return PencilView(context)
    }

    override fun getFragment(): Fragment {
        return RectangleFragment()
    }
}