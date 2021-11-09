package com.example.colorimagemobile.ui.home.fragments.drawing.attributes.rectangle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.RadioGroup
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.drawing.toolsAttribute.PencilService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

class RectangleFragment : Fragment() {
    private lateinit var borderWidthPicker: NumberPicker

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rectangle, container, false)
        onRectRadioListener(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBorderWidthPicker(view)
    }

    private fun setBorderWidthPicker(view: View) {
        borderWidthPicker = view.findViewById(R.id.rec_border_width_picker)
        borderWidthPicker.minValue = PencilService.minWidth
        borderWidthPicker.maxValue = PencilService.maxWidth
        borderWidthPicker.value = PencilService.currentWidth

        borderWidthPicker.setOnValueChangedListener { numberPicker, oldValue, newValue ->
            printMsg(newValue.toString() + " new border")
//            PencilService.currentWidth = newValue
        }
    }

    fun onRectRadioListener(view: View) {
        val radioGroup: RadioGroup = view.findViewById(R.id.rectangle_radioGroup)

        radioGroup.setOnCheckedChangeListener { _, checkedId -> // checkedId is the RadioButton selected
            when (checkedId) {
                R.id.rect_border_fill -> printMsg("fill")
                R.id.rect_no_border -> printMsg("no border")
                R.id.rect_only_border -> printMsg("onlyyy border")
            }
        }
    }
}