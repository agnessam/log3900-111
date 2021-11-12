package com.example.colorimagemobile.ui.home.fragments.gallery.attributes.colorPalette

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import top.defaults.colorpicker.ColorPickerView
import java.lang.String

class ColorPaletteFragment : Fragment(R.layout.fragment_color_palette) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colorPicker: ColorPickerView = view.findViewById(R.id.colorPicker)
        colorPicker.setInitialColor(ColorService.getColorAsInt())

        colorPicker.subscribe { color, _, _ ->
            val rgbaColor = ColorService.intToRGB(color)
            ColorService.setColor(rgbaColor)
        }
    }
}