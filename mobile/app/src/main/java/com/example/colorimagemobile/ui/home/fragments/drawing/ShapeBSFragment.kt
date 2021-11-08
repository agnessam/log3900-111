//package com.example.colorimagemobile.ui.home.fragments.drawing
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import android.widget.ImageView
//import android.widget.RadioGroup
//import android.widget.SeekBar
//import android.widget.SeekBar.OnSeekBarChangeListener
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.colorimagemobile.R
//import com.example.colorimagemobile.Shape.ShapeType
//import com.example.colorimagemobile.adapter.ColorPickerAdapter
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//
//class ShapeBSFragment (private val recyclerView: RecyclerView, private val frameLayout: FrameLayout): Fragment(), OnSeekBarChangeListener {
//    private var mProperties: Properties? = null
//
//    interface Properties {
//        fun onColorChanged(colorCode: Int)
//        fun onOpacityChanged(opacity: Int)
//        fun onShapeSizeChanged(shapeSize: Int)
//        fun onShapePicked(shapeType: ShapeType?)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_bottom_shapes_dialog, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val rvColor: RecyclerView = view.findViewById(R.id.shapeColors)
//        val sbOpacity = view.findViewById<SeekBar>(R.id.shapeOpacity)
//        val sbBrushSize = view.findViewById<SeekBar>(R.id.shapeSize)
//        val imgCancel = view.findViewById<ImageView>(R.id.imgCancel)
//
//        sbOpacity.setOnSeekBarChangeListener(this)
//        sbBrushSize.setOnSeekBarChangeListener(this)
//        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        rvColor.layoutManager = layoutManager
//        rvColor.setHasFixedSize(true)
//        val colorPickerAdapter = activity?.let { ColorPickerAdapter(it) }
//        colorPickerAdapter!!.setOnColorPickerClickListener(object : ColorPickerAdapter.OnColorPickerClickListener {
//           override fun onColorPickerClickListener(colorCode: Int) {
//                if (mProperties != null) {
//                    mProperties!!.onColorChanged(colorCode)
//                }
//            }
//        })
//        rvColor.adapter = colorPickerAdapter
//
//        imgCancel.setOnClickListener {
//            frameLayout!!.visibility = View.GONE
//            recyclerView!!.visibility = View.VISIBLE
//        }
//    }
//
//    fun setPropertiesChangeListener(properties: Properties?) {
//        mProperties = properties
//    }
//
//    override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//        when (seekBar.id) {
//            R.id.shapeOpacity -> if (mProperties != null) {
//                mProperties!!.onOpacityChanged(i)
//            }
//            R.id.shapeSize -> if (mProperties != null) {
//                mProperties!!.onShapeSizeChanged(i)
//            }
//        }
//    }
//
//    override fun onStartTrackingTouch(seekBar: SeekBar) {}
//    override fun onStopTrackingTouch(seekBar: SeekBar) {}
//}