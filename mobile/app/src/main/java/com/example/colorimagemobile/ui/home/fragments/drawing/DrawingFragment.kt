package com.example.colorimagemobile.ui.home.fragments.drawing

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.drawing.ToolService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

class DrawingFragment : Fragment(R.layout.fragment_drawing) {
    private lateinit var drawingFragment: ConstraintLayout;

      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val canvasView = context?.let { PencilView(it) }
          drawingFragment = view.findViewById(R.id.drawingFragment)
          view.findViewById<RelativeLayout>(R.id.canvas_view).addView(canvasView)

          addToolsOnSidebar()
          setToolsListener()
    }

    private fun addToolsOnSidebar() {
        ToolService.getAllTools().forEach { tool ->
            val toolBtn = Button(context)
            toolBtn.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            toolBtn.text = tool.tool
            toolBtn.setBackgroundColor(Color.GREEN)
            toolBtn.setOnClickListener { ToolService.setCurrentTool(tool.index) }

            val toolSidebar = drawingFragment.findViewById<LinearLayout>(R.id.canvas_tools)
            toolSidebar.addView(toolBtn)
        }
    }

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

                else -> PencilView(context)
            }

            val canvasLayout = drawingFragment.findViewById<RelativeLayout>(R.id.canvas_view)
            canvasLayout.removeAllViews()
            canvasLayout.addView(canvasView)
        })
    }
}