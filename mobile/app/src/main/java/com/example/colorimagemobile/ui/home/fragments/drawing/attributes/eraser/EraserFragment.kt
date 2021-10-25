package com.example.colorimagemobile.ui.home.fragments.drawing.attributes.eraser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.drawing.toolsAttribute.EraserService

class EraserFragment : Fragment(R.layout.fragment_eraser) {

    private lateinit var widthPicker: NumberPicker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        widthPicker = view.findViewById(R.id.eraser_width_picker)
        widthPicker.minValue = EraserService.MIN_VALUE
        widthPicker.maxValue = EraserService.MAX_VALUE
        widthPicker.value = EraserService.getCurrentWidth().value!!

        widthPicker.setOnValueChangedListener { numberPicker, oldValue, newValue ->
            EraserService.setCurrentWidth(newValue)
        }
    }
}