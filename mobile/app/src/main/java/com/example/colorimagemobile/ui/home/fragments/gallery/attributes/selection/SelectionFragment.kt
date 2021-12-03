package com.example.colorimagemobile.ui.home.fragments.gallery.attributes.selection

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.services.drawing.toolsAttribute.DeleteService
import com.example.colorimagemobile.services.drawing.toolsAttribute.LineWidthService
import com.example.colorimagemobile.services.drawing.toolsAttribute.SelectionColorService
import com.google.android.material.button.MaterialButton
import top.defaults.colorpicker.ColorPickerView

class SelectionFragment : Fragment(R.layout.fragment_selection) {
    private lateinit var  deleteShapeBtn: Button
    private lateinit var  widthPicker: NumberPicker

    private lateinit var primaryBtn: Button
    private lateinit var secondaryBtn: Button
    private lateinit var swapBtn: MaterialButton
    private lateinit var colorPicker: ColorPickerView
    private lateinit var primaryColorPreview: RelativeLayout
    private lateinit var secondaryColorPreview: RelativeLayout

    private lateinit var currentColor: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVariables(view)
        setWidthListener()
        setDeleteListener()
        setColorListeners()
    }

    private fun initVariables(view: View) {
        deleteShapeBtn = view.findViewById(R.id.deleteShapeBtn)
        widthPicker = view.findViewById(R.id.width_picker)
        colorPicker = view.findViewById(R.id.colorPicker)
        primaryBtn = view.findViewById(R.id.colorPrimaryColor)
        secondaryBtn = view.findViewById(R.id.colorSecondaryColor)
        swapBtn = view.findViewById(R.id.colorSwapColor)
        primaryColorPreview = view.findViewById(R.id.colorPalettePrimaryColor)
        secondaryColorPreview = view.findViewById(R.id.colorPaletteSecondaryColor)

        currentColor = SelectionColorService.getPrimaryColorAsString()
        colorPicker.setInitialColor(SelectionColorService.getPrimaryColorAsInt())
    }

    private fun setWidthListener() {
        widthPicker.minValue = LineWidthService.minWidth
        widthPicker.maxValue = LineWidthService.maxWidth

        LineWidthService.getCurrentWidth().observe(context as LifecycleOwner, {
            widthPicker.value = it
        })

        widthPicker.setOnValueChangedListener { _, _, newValue ->
            LineWidthService.changeLineWidth(newValue)
        }
    }

    private fun setDeleteListener() {
        deleteShapeBtn.setOnClickListener {
            DeleteService.deleteSelectedLayer()
        }
    }

    private fun setColorListeners() {
        SelectionColorService.getCurrentPrimaryColor().observe(context as LifecycleOwner, {
            primaryColorPreview.setBackgroundColor(ColorService.rgbaToInt(it))
        })
        SelectionColorService.getCurrentSecondaryColor().observe(context as LifecycleOwner, {
            secondaryColorPreview.setBackgroundColor(ColorService.rgbaToInt(it))
        })

        colorPicker.subscribe { color, _, _ ->
            currentColor = ColorService.intToRGBA(color)
        }

        primaryBtn.setOnClickListener {
            SelectionColorService.setPrimaryColor(currentColor)
            updatePreview()
        }

        secondaryBtn.setOnClickListener {
            SelectionColorService.setSecondaryColor(currentColor)
            updatePreview()
        }

        swapBtn.setOnClickListener {
            SelectionColorService.swapColors()
            updatePreview()
        }
    }

    private fun updatePreview() {
        primaryColorPreview.setBackgroundColor(SelectionColorService.getPrimaryColorAsInt())
        secondaryColorPreview.setBackgroundColor(SelectionColorService.getSecondaryColorAsInt())
    }
}