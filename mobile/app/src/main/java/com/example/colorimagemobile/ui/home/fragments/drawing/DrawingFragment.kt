package com.example.colorimagemobile.ui.home.fragments.drawing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.Interface.OnItemClick
import com.example.colorimagemobile.Interface.Editor
import com.example.colorimagemobile.R
import com.example.colorimagemobile.Shape.*
import com.example.colorimagemobile.adapter.MainAdapter
import com.example.colorimagemobile.models.ToolModel
import com.example.colorimagemobile.Shape.EditorViewState


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DrawingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DrawingFragment : Fragment(), OnItemClick,

     ShapeBSFragment.Properties {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mShapeBSFragment: ShapeBSFragment? = null
    private val mToolList: ArrayList<ToolModel> = ArrayList()
    var mainAdapter: MainAdapter? = null
    var mEditor: Editor? = null
    var mEditorView: EditorView? = null
    var mShapeBuilder: ShapeBuilder? = null
    var recyclerView: RecyclerView? = null
    var rvColor: RecyclerView? = null
    var sbOpacity: SeekBar? = null
    var sbBrushSize: SeekBar? = null
    var relativeLayout: FrameLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_drawing, container, false)
        mEditorView = view.findViewById(R.id.editorView)
        recyclerView = view.findViewById(R.id.mainRecyclerView)

         rvColor = view.findViewById(R.id.shapeColors)
         sbOpacity = view.findViewById(R.id.shapeOpacity)
         sbBrushSize = view.findViewById(R.id.shapeSize)
         relativeLayout = view.findViewById(R.id.relativeLayout2)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setMainAdapter()
        mShapeBuilder = ShapeBuilder()
        mShapeBSFragment = ShapeBSFragment(recyclerView!!, relativeLayout!!)
        mShapeBSFragment!!.setPropertiesChangeListener(this)

        mEditor = Editor.Builder(requireActivity(), mEditorView!!)
            .build()

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DrawingFragment.
         */
        fun newInstance(param1: String, param2: String) =
            DrawingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun setMainAdapter() {

        mToolList.add(ToolModel("Shape", R.drawable.ic_oval))
        mToolList.add(ToolModel("Eraser", R.drawable.ic_eraser))
        mToolList.add(ToolModel("brush", R.drawable.ic_brush))
        mToolList.add(ToolModel("rectangle", R.drawable.ic_rectangle))
        mToolList.add(ToolModel("ellipse", R.drawable.ic_ellipse_outline))
        // don't delete to be use
//        mToolList.add(ToolModel("clearView", R.drawable.ic_delete_empty_outline))

        recyclerView?.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL,
            false
        )
        mainAdapter = MainAdapter(mToolList, requireActivity(), this)
        recyclerView?.adapter = mainAdapter

    }

    override fun onItemClick(position: Int) {
        if (position == 0) {
            mEditor!!.setBrushDrawingMode(true)
            mEditor!!.setShape(mShapeBuilder)
            relativeLayout!!.visibility = View.VISIBLE
            recyclerView!!.visibility = View.GONE
            setFragment(mShapeBSFragment!!)
        } else if (position == 1) {
//            mEditor!!.brushEraser()
        }
        else if (position == 2 ) {
            mEditor!!.setBrushDrawingMode(true)
            mShapeBuilder!!.withShapeType(ShapeType.BRUSH)
            mEditor!!.setShape(mShapeBuilder)

        }

        else if (position == 3 ) {
            mEditor!!.setBrushDrawingMode(true)
            mShapeBuilder!!.withShapeType(ShapeType.RECTANGLE)
            mEditor!!.setShape(mShapeBuilder)

        }

        else if (position == 4 ) {
            mEditor!!.setBrushDrawingMode(true)
            onShapePicked(ShapeType.ELLIPSE)
            mShapeBuilder!!.withShapeType(ShapeType.ELLIPSE)
            mEditor!!.setShape(mShapeBuilder)

        }
        // don't delete
//        else if (position == 5 ) {
//            EditorViewState().clearAddedViews()
//            DrawingPreview().drawShapes.clear()
//
//        }

    }


    fun setFragment(fr : Fragment){
        val frag = requireActivity().supportFragmentManager.beginTransaction()
        frag.replace(R.id.relativeLayout2,fr)
        frag.commit()
    }

    override fun onColorChanged(colorCode: Int) {
        mEditor!!.setShape(mShapeBuilder!!.withShapeColor(colorCode))
    }

    override fun onOpacityChanged(opacity: Int) {
        mEditor!!.setShape(mShapeBuilder!!.withShapeOpacity(opacity))
    }

    override fun onShapeSizeChanged(shapeSize: Int) {
        mEditor!!.setShape(mShapeBuilder!!.withShapeSize(shapeSize.toFloat()))
    }

    override fun onShapePicked(shapeType: ShapeType?) {
        mEditor!!.setShape(mShapeBuilder!!.withShapeType(shapeType))
    }

}