package com.example.colorimagemobile.Interface

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.classes.Shape.DrawingPreview
import com.example.colorimagemobile.enumerators.ToolType

interface ITool {
    fun getTitle(): String
    fun getType(): ToolType
    fun getIcon(): Int
    fun getView(context: Context): DrawingPreview?
    fun getFragment(): Fragment
}