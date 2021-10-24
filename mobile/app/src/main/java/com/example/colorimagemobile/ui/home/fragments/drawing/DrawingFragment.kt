package com.example.colorimagemobile.ui.home.fragments.drawing

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.drawing.ToolService

class DrawingFragment : Fragment(R.layout.fragment_drawing) {
    private lateinit var drawingFragment: ConstraintLayout;

      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawingFragment = view.findViewById(R.id.drawingFragment)

        addToolsOnSidebar()
        setToolsListener()
    }

    // dynamically add tools on sidebar
    private fun addToolsOnSidebar() {
        ToolService.getAllTools().forEach { tool ->
            val toolBtn = Button(context)
            toolBtn.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            toolBtn.setCompoundDrawablesWithIntrinsicBounds(tool.icon, 0, 0, 0)
            toolBtn.setBackgroundColor(Color.rgb(245, 245, 245))
            toolBtn.setOnClickListener { ToolService.setCurrentTool(tool.index) }

            val toolSidebar = drawingFragment.findViewById<LinearLayout>(R.id.canvas_tools)
            toolSidebar.addView(toolBtn)
        }
    }

    // update tool when changed tool
    private fun setToolsListener() {
        ToolService.getCurrentTool().observe(viewLifecycleOwner, { tool ->
            val canvasView: CanvasView;
            val context = requireContext()

            /**
             * @todo: add corresponding class dynamically
             */
            canvasView = when (tool.index) {
                0 -> PencilView(context)
                1 -> EraserView(context)

                else -> PencilView(context) // default is pencil
            }

            val canvasLayout = drawingFragment.findViewById<RelativeLayout>(R.id.canvas_view)
            canvasLayout.removeAllViews()
            canvasLayout.addView(canvasView)
        })
    }
}