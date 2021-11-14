package com.example.colorimagemobile.ui.home.fragments.gallery.attributes.ellipse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.RadioGroup
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.drawing.toolsAttribute.EllipseService
import com.example.colorimagemobile.services.drawing.toolsAttribute.EllipseStyle


class EllipseFragment : Fragment() {
    private lateinit var borderWidthPicker: NumberPicker

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_ellipse, container, false)
        onRectRadioListener(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBorderWidthPicker(view)
    }

    private fun setBorderWidthPicker(view: View) {
        borderWidthPicker = view.findViewById(R.id.rec_border_width_picker)
        borderWidthPicker.minValue = EllipseService.minWidth
        borderWidthPicker.maxValue = EllipseService.maxWidth
        borderWidthPicker.value = EllipseService.currentWidth

        // set border width
        borderWidthPicker.setOnValueChangedListener { _, _, newValue ->
            EllipseService.currentWidth = newValue
        }
    }

    private fun onRectRadioListener(view: View) {
        val radioGroup: RadioGroup = view.findViewById(R.id.ellipse_radioGroup)

        // apply border style
        radioGroup.setOnCheckedChangeListener { _, checkedId -> // checkedId is the RadioButton selected
            val borderStyle = when (checkedId) {
                R.id.rect_border_fill -> EllipseStyle.WITH_BORDER_FILL
                R.id.rect_no_border -> EllipseStyle.NO_BORDER
                R.id.rect_only_border -> EllipseStyle.ONLY_BORDER
                else -> EllipseStyle.WITH_BORDER_FILL
            }

            EllipseService.updateBorderStyle(borderStyle)
        }
    }
}