package com.example.colorimagemobile.bottomsheets

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.CommonFun.Companion.toggleButton
import com.example.colorimagemobile.utils.Constants
import com.example.colorimagemobile.utils.Constants.DRAWING.Companion.MAX_HEIGHT
import com.example.colorimagemobile.utils.Constants.DRAWING.Companion.MAX_WIDTH
import com.example.colorimagemobile.utils.Constants.DRAWING.Companion.MIN_HEIGHT
import com.example.colorimagemobile.utils.Constants.DRAWING.Companion.MIN_WIDTH
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import top.defaults.colorpicker.ColorPickerView
import java.io.Serializable

class NewDrawingMenuBottomSheet: BottomSheetDialogFragment() {
    private lateinit var createDrawingBtn: Button
    private lateinit var widthLayout: TextInputLayout
    private lateinit var heightLayout: TextInputLayout
    private lateinit var dialog: BottomSheetDialog

    private var widthValue = 0
    private var heightValue = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottomsheet_drawing_menu, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    private fun closeSheet() { dialog.behavior.state = BottomSheetBehavior.STATE_HIDDEN }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createDrawingBtn = view.findViewById(R.id.createDrawingBtn)
        widthLayout = view.findViewById(R.id.newDrawingWidthInputLayout)
        heightLayout = view.findViewById(R.id.newDrawingHeightInputLayout)

        toggleButton(createDrawingBtn, false)
        fetchTeams()
        setListeners(view)
    }

    private fun fetchTeams() {
        val sharedPrefService = SharedPreferencesService(requireContext())
        val token = sharedPrefService.getItem(Constants.STORAGE_KEY.TOKEN)

        UserRepository().getUserTeams(token, UserService.getUserInfo()._id).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                return@observe
            }

            val teams: List<TeamModel> = it.data as List<TeamModel>
            printMsg(teams.toString())
        })
    }

    private fun setListeners(view: View) {
        var color: Int = Color.WHITE

        // width input validation
        view.findViewById<TextInputEditText>(R.id.newDrawingWidthInputText).doOnTextChanged { text, _, _, _ ->
            widthValue = getCurrentValue(text)
            widthLayout.error = getErrorMessage(widthValue, MIN_WIDTH, MAX_WIDTH)
            updateCreateBtn()
        }

        // height input validation
        view.findViewById<TextInputEditText>(R.id.newDrawingHeightInputText).doOnTextChanged { text, _, _, _ ->
            heightValue = getCurrentValue(text)
            heightLayout.error = getErrorMessage(heightValue, MIN_HEIGHT, MAX_HEIGHT)
            updateCreateBtn()
        }

        view.findViewById<ColorPickerView>(R.id.colorPickerNewDrawing).subscribe { newColor, _, _ ->
            color = newColor
        }

        view.findViewById<Button>(R.id.createDrawingBtn).setOnClickListener {
            CanvasService.setWidth(widthValue)
            CanvasService.setHeight(heightValue)

            val bitmap = Bitmap.createBitmap(widthValue, heightValue, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(color)

            val base64URI = ImageConvertor(requireContext()).bitmapToBase64(bitmap)
            val ownerModel = "User"
            val ownerId = ""
            val drawingName = "MyDrawing"

//            closeSheet()

//            val fragmentManager = MyFragmentManager(requireActivity())
//            fragmentManager.closeFragment(this@NewDrawingMenuBottomSheet)
//            fragmentManager.openWithData(R.id.main_gallery_fragment, GalleryDrawingFragment(), NewDrawing(canvas, bitmap))
        }
    }

    // read input field and convert to int
    private fun getCurrentValue(text: CharSequence?): Int {
        val currentText = text!!.toString()
        return if(currentText == "") 0 else currentText.toInt()
    }

    // check input field's value range
    private fun getErrorMessage(currentValue: Int, min: Int, max: Int): String? {
        return if (currentValue < min || currentValue > max) getSizeError(min, max) else null
    }

    // error to show if input is invalid
    private fun getSizeError(min: Int, max: Int): String {
        return "Value must be between ${min}px and ${max}px"
    }

    // activate/deactivate button depending on input fields
    private fun updateCreateBtn() {
        val areLayoutsValid = widthLayout.error == null && heightLayout.error == null
        val areInputsValid = widthValue > 0 && heightValue > 0
        toggleButton(createDrawingBtn, areLayoutsValid && areInputsValid)
    }
}