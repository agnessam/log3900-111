package com.example.colorimagemobile.ui.home.fragments.drawing

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import com.example.colorimagemobile.Interface.Editor
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.drawingEditor.EditorViewClass
import com.example.colorimagemobile.classes.Shape.ShapeBuilder
import com.example.colorimagemobile.classes.Shape.ShapeType
import com.example.colorimagemobile.classes.tools.ToolsFactory
import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.services.drawing.ToolTypeService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

class DrawingFragment : Fragment(R.layout.fragment_drawing) {
    private lateinit var panelView: CardView
    private lateinit var toolsFactory: ToolsFactory
    private var canvasView : EditorViewClass? = null
    var viewEditor: Editor? = null
    private  var canvasTool : LinearLayout ? = null
    var mShapeBuilder: ShapeBuilder? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_drawing, container, false)
        canvasView = view.findViewById(R.id.canvas_view)
        panelView = view.findViewById<CardView>(R.id.canvas_tools_attributes_cardview)
        canvasTool = view.findViewById<LinearLayout>(R.id.canvas_tools)

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolsFactory = ToolsFactory()
        viewEditor = Editor.Builder(requireActivity(), canvasView!!).build()
        viewEditor!!.setBrushDrawingMode(true)
        mShapeBuilder = ShapeBuilder()
        addToolsOnSidebar()
        setToolsListener()
    }

    // dynamically add tools on sidebar
    private fun addToolsOnSidebar() {
        ToolTypeService.getAllToolTypes().forEach { toolType ->
            val tool = toolsFactory.getTool(toolType)

            // create dynamic button for each tool
            val toolBtn = Button(context)
            toolBtn.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            toolBtn.setBackgroundColor(Color.rgb(245, 245, 245))

            // center button
            toolBtn.text = SpannableString(" ").apply {
                setSpan(
                    ImageSpan(requireContext(), tool.getIcon()),
                    0,
                    1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            // handle attribute panel when clicked on tool
            toolBtn.setOnClickListener {
                togglePanel(tool.getType())
                setPanelAttribute(tool.getFragment())
                panelView.findViewById<TextView>(R.id.tool_name).text = tool.getTitle()
            }

            canvasTool?.addView(toolBtn)
        }
    }

    // update tool when changed tool
    private fun setToolsListener() {
        ToolTypeService.getCurrentToolType().observe(viewLifecycleOwner, { toolType ->
            val context = requireContext()

            // client
            val toolView = toolsFactory.getTool(toolType).getView(context)

            when (toolsFactory.getTool(toolType).getTitle()) {

                "PENCIL" -> { printMsg("pencil choose")
                              viewEditor!!.setBrushDrawingMode(true)
                              mShapeBuilder!!.withShapeType(ShapeType.BRUSH)
                              viewEditor!!.setShape(mShapeBuilder)
                            }

                "RECTANGLE" ->  { viewEditor!!.setBrushDrawingMode(true)
                                  mShapeBuilder!!.withShapeType(ShapeType.RECTANGLE)
                                  viewEditor!!.setShape(mShapeBuilder)
                                  ("rectangle choose")
                                 }
                else -> { // choose pencil
                        printMsg("else")
                        viewEditor!!.setBrushDrawingMode(true)
                        mShapeBuilder!!.withShapeType(ShapeType.BRUSH)
                        viewEditor!!.setShape(mShapeBuilder)

                    printMsg("else no tool choose")
                }
            }

            if (toolView != null) {
                printMsg("toolview is null")
                val canvasLayout = canvasView
                canvasLayout?.removeAllViews()
                canvasLayout?.addView(toolView)
            }
        })
    }

    // open/close side attributes panel
    private fun togglePanel(toolType: ToolType) {
        val currentToolType = ToolTypeService.getCurrentToolType().value

        // close panel because toggling on same tool
        if (currentToolType == toolType && panelView.visibility == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(panelView, AutoTransition())
            panelView.visibility = View.GONE
            return
        }

        // new tool clicked -> open panel
        ToolTypeService.setCurrentToolType(toolType)
        TransitionManager.beginDelayedTransition(panelView, AutoTransition())
        panelView.visibility = View.VISIBLE
    }

    // update tools attributes panel fragment
    private fun setPanelAttribute(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.tool_attribute_fragment, fragment)
            ?.commitAllowingStateLoss()
    }

//     fun onColorChanged(colorCode: Int) {
//        viewEditor!!.setShape(mShapeBuilder!!.withShapeColor(colorCode))
//    }
//
//     fun onOpacityChanged(opacity: Int) {
//        viewEditor!!.setShape(mShapeBuilder!!.withShapeOpacity(opacity))
//    }
//
//     fun onShapeSizeChanged(shapeSize: Int) {
//        viewEditor!!.setShape(mShapeBuilder!!.withShapeSize(shapeSize.toFloat()))
//    }
//
//     fun onShapePicked(shapeType: ShapeType?) {
//        viewEditor!!.setShape(mShapeBuilder!!.withShapeType(shapeType))
//    }
}