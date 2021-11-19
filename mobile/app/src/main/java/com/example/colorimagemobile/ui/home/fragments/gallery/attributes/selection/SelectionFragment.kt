package com.example.colorimagemobile.ui.home.fragments.gallery.attributes.selection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.toolsCommand.EllipseCommand
import com.example.colorimagemobile.classes.toolsCommand.PencilCommand
import com.example.colorimagemobile.classes.toolsCommand.RectangleCommand
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.services.drawing.DrawingObjectManager
import com.example.colorimagemobile.services.drawing.toolsAttribute.*

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
        widthPicker.minValue = LineWidthService.minWidth
        widthPicker.maxValue = LineWidthService.maxWidth

        // TODO: make the width picker value update automatically on select
        if (SelectionService.selectedShapeIndex != -1) {
            val command = DrawingObjectManager.getCommand(SelectionService.selectedShapeIndex)
            when(command) {
                is PencilCommand -> {
                    widthPicker.value = command.pencil.strokeWidth
                }
                is RectangleCommand -> {
                    widthPicker.value = command.rectangle.strokeWidth
                }
                is EllipseCommand -> {
                    widthPicker.value = command.ellipse.strokeWidth
                }
            }
        } else {
            widthPicker.value = 0
        }

        widthPicker.setOnValueChangedListener { _, _, newValue ->
            LineWidthService.changeLineWidth(newValue)
        }
    }

    private fun setDeleteListener() {
        deleteShapeBtn.setOnClickListener {
            DeleteService.deleteSelectedLayer()
        }
    }
}