package com.example.colorimagemobile.bottomsheets

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.classes.xml_json.SVGBuilder
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.TeamModel
import com.example.colorimagemobile.repositories.DrawingRepository
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.services.drawing.CanvasService
import com.example.colorimagemobile.services.drawing.CanvasUpdateService
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.services.drawing.toolsAttribute.ColorService
import com.example.colorimagemobile.ui.home.fragments.gallery.GalleryDrawingFragment
import com.example.colorimagemobile.utils.CommonFun.Companion.hideKeyboard
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants.DRAWING.Companion.MAX_HEIGHT
import com.example.colorimagemobile.utils.Constants.DRAWING.Companion.MAX_WIDTH
import com.example.colorimagemobile.utils.Constants.DRAWING.Companion.MIN_HEIGHT
import com.example.colorimagemobile.utils.Constants.DRAWING.Companion.MIN_WIDTH
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.bottomsheet_create_channel.*
import kotlinx.android.synthetic.main.bottomsheet_drawing_menu.*
import top.defaults.colorpicker.ColorPickerView

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

        fetchTeams()
        setListeners(view)
    }

    private fun fetchTeams() {
        UserRepository().getUserTeams(UserService.getToken(), UserService.getUserInfo()._id).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                return@observe
            }

            val teams: List<TeamModel> = it.data as List<TeamModel>
            printMsg(teams.toString())
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners(view: View) {
        var color = "rgba(255, 255, 255, 1)"

        createNewDrawingForm.setOnTouchListener{v, event -> hideKeyboard(requireContext(), createNewDrawingForm)}

        // width input validation
        view.findViewById<TextInputEditText>(R.id.newDrawingWidthInputText).doOnTextChanged { text, _, _, _ ->
            widthValue = getCurrentValue(text)
            widthLayout.error = getErrorMessage(widthValue, MIN_WIDTH, MAX_WIDTH)

        }

        // height input validation
        view.findViewById<TextInputEditText>(R.id.newDrawingHeightInputText).doOnTextChanged { text, _, _, _ ->
            heightValue = getCurrentValue(text)
            heightLayout.error = getErrorMessage(heightValue, MIN_HEIGHT, MAX_HEIGHT)
        }

        view.findViewById<ColorPickerView>(R.id.colorPickerNewDrawing).subscribe { newColor, _, _ ->
            color = ColorService.intToRGB(newColor)
        }

        view.findViewById<Button>(R.id.createDrawingBtn).setOnClickListener {
            if (widthValue==0 || heightValue==0){
            val shake = AnimationUtils.loadAnimation(requireActivity().getApplicationContext(), R.anim.shake)
            createNewDrawingForm.startAnimation(shake);
                return@setOnClickListener
        }
            CanvasService.setWidth(widthValue)
            CanvasService.setHeight(heightValue)

            // create SVG object
            val svgBuilder = SVGBuilder()
            svgBuilder.addAttr("width", CanvasService.getWidth())
            svgBuilder.addAttr("height", CanvasService.getHeight())
            svgBuilder.addAttr("style", "background-color: $color")

            val base64 = ImageConvertor(requireContext()).XMLToBase64(svgBuilder.getXML())

            // to change dynamically once lourd has completed the UI
            val newDrawing = DrawingModel.CreateDrawing(_id = null, dataUri = base64, ownerModel = "User", ownerId = "", name = "MyDrawing2")

            DrawingRepository().createNewDrawing(newDrawing).observe(context as LifecycleOwner, {
                if (it.isError as Boolean) {
                    return@observe
                }

                // open drawing
                val drawing = it.data as DrawingModel.CreateDrawing
                if (drawing._id != null) {
                    closeSheet()

                    DrawingService.setCurrentDrawingID(drawing._id)
                    MyFragmentManager(context as FragmentActivity).open(R.id.main_gallery_fragment, GalleryDrawingFragment())

                    CanvasService.createNewBitmap()
                    CanvasService.updateCanvasColor(ColorService.rgbaToInt(color))
                    CanvasUpdateService.invalidate()
                }
            })
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

}