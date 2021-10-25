package com.example.colorimagemobile.ui.home.fragments.drawing.attributes.pencil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.NumberPicker
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService

class PencilFragment : Fragment(R.layout.fragment_pencil) {

    private lateinit var widthPicker: NumberPicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        widthPicker = view.findViewById(R.id.pencil_width_picker)
        widthPicker.minValue = PencilService.MIN_VALUE
        widthPicker.maxValue = PencilService.MAX_VALUE
        widthPicker.value = PencilService.getCurrentWidth().value!!

        widthPicker.setOnValueChangedListener { numberPicker, oldValue, newValue ->
            PencilService.setCurrentWidth(newValue)
        }
    }
}