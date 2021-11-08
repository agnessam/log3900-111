package com.example.colorimagemobile.classes.tools

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.R
import com.example.colorimagemobile.Shape.DrawingPreview
import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.interfaces.ITool
import com.example.colorimagemobile.ui.home.fragments.drawing.attributes.rectangle.RectangleFragment
import com.example.colorimagemobile.ui.home.fragments.drawing.views.RectangleView

class RectangleTool : ITool {
    override fun getTitle(): String {
        return "rectangle"
    }

    override fun getType(): ToolType {
        return ToolType.RECTANGLE
    }

    override fun getIcon(): Int {
        return R.drawable.ic_rectangle
    }

    override fun getView(context: Context): DrawingPreview {
        return RectangleView(context)
    }

    override fun getFragment(): Fragment {
        return RectangleFragment()
    }
}