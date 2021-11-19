package com.example.colorimagemobile.ui.home.fragments.gallery.attributes.selection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.drawing.toolsAttribute.DeleteService
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import com.example.colorimagemobile.services.drawing.toolsAttribute.RectangleService
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionService

class SelectionFragment : Fragment(R.layout.fragment_selection) {
    private lateinit var  deleteShapeBtn: Button
    private lateinit var  widthPicker: NumberPicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deleteShapeBtn = view.findViewById(R.id.deleteShapeBtn)
        widthPicker = view.findViewById(R.id.width_picker)
        setWidthListener()
        setDeleteListener()
    }

    private fun setWidthListener() {
        widthPicker.minValue = SelectionService.minWidth
        widthPicker.maxValue = SelectionService.maxWidth
        widthPicker.value = SelectionService.currentWidth

        widthPicker.setOnValueChangedListener { _, _, newValue ->
            SelectionService.currentWidth = newValue
        }
    }

    private fun setDeleteListener() {
        deleteShapeBtn.setOnClickListener {
            DeleteService.deleteSelectedLayer()
        }
    }
}