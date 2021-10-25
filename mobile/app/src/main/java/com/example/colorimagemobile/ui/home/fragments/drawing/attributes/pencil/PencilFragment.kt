package com.example.colorimagemobile.ui.home.fragments.drawing.attributes.pencil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.NumberPicker
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.drawing.ToolSettingsService

class PencilFragment : Fragment(R.layout.fragment_pencil) {

    private lateinit var widthPicker: NumberPicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        widthPicker = view.findViewById(R.id.width_picker)
        widthPicker.minValue = 2
        widthPicker.maxValue = 30
        widthPicker.value = ToolSettingsService.currentWidth.value!!

        widthPicker.setOnValueChangedListener { numberPicker, oldValue, newValue ->
            ToolSettingsService.setCurrentWidth(newValue)
        }
    }
}