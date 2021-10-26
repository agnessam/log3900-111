package com.example.colorimagemobile.ui.home.fragments.drawing

import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.colorimagemobile.R
import com.example.colorimagemobile.enumerators.ToolType
import com.example.colorimagemobile.services.drawing.ToolService

class DrawingFragment : Fragment(R.layout.fragment_drawing) {
    private lateinit var drawingFragment: ConstraintLayout;
    private lateinit var panelView: CardView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawingFragment = view.findViewById(R.id.drawingFragment)
        panelView = drawingFragment.findViewById<CardView>(R.id.canvas_tools_attributes_cardview)

        addToolsOnSidebar()
        setToolsListener()
    }

    // dynamically add tools on sidebar
    private fun addToolsOnSidebar() {
        ToolService.getAllTools().forEach { command ->
            val toolBtn = Button(context)
            toolBtn.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            toolBtn.setCompoundDrawablesWithIntrinsicBounds(command.getIcon(), 0, 0, 0)
            toolBtn.setBackgroundColor(Color.rgb(245, 245, 245))
            toolBtn.setOnClickListener {
                // handle attribute panel when clicked on tool
                togglePanel(command.getType())
                setPanelAttribute(command.getFragment())
                panelView.findViewById<TextView>(R.id.tool_name).text = command.getType().title
            }

            val toolSidebar = drawingFragment.findViewById<LinearLayout>(R.id.canvas_tools)
            toolSidebar.addView(toolBtn)
        }
    }

    // update tool when changed tool
    private fun setToolsListener() {
        ToolService.getCurrentTool().observe(viewLifecycleOwner, { command ->
            val context = requireContext()

            val toolView = command.getView(context)
            if (toolView != null) {
                val canvasLayout = drawingFragment.findViewById<RelativeLayout>(R.id.canvas_view)
                canvasLayout.removeAllViews()
                canvasLayout.addView(toolView)
            }
        })
    }

    private fun togglePanel(toolType: ToolType) {
        // close panel because toggling on same tool
        if (ToolService.getCurrentTool().value?.getType() == toolType && panelView.visibility == View.VISIBLE) {
            TransitionManager.beginDelayedTransition(panelView, AutoTransition())
            panelView.visibility = View.GONE
            return
        }

        // new tool clicked -> open panel
        ToolService.setCurrentTool(toolType)
        TransitionManager.beginDelayedTransition(panelView, AutoTransition())
        panelView.visibility = View.VISIBLE
    }

    // update tools attributes panel fragment
    private fun setPanelAttribute(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.tool_attribute_fragment, fragment)
            ?.commitAllowingStateLoss()
    }
}