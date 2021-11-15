package com.example.colorimagemobile.ui.home.fragments.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.repositories.DrawingRepository
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.utils.Constants
import java.util.*
import android.graphics.Bitmap
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.adapter.DrawingMenuRecyclerAdapter
import com.example.colorimagemobile.bottomsheets.NewDrawingMenuBottomSheet
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import kotlin.collections.ArrayList

private const val NB_ROWS = 3

class GalleryMenuFragment : Fragment(R.layout.fragment_gallery_menu) {
    private lateinit var drawingRepo: DrawingRepository
    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var drawings: List<DrawingModel.Drawing>
    private lateinit var galleryView: ConstraintLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawingRepo = DrawingRepository()
        sharedPreferencesService = context?.let { SharedPreferencesService(it) }!!
        galleryView = view.findViewById(R.id.galleryMenuView)
        drawings = arrayListOf()

        MyFragmentManager(requireActivity()).hideBackButton()
        DrawingService.setCurrentDrawingID(null)
        setListeners()
        getAllDrawings()
    }

    private fun setListeners() {
        val createDrawingBtn: Button = galleryView.findViewById(R.id.newDrawingBtn)
        createDrawingBtn.setOnClickListener {
            val newDrawingMenu = NewDrawingMenuBottomSheet()
            newDrawingMenu.show(parentFragmentManager, "NewDrawingBottomSheetDialog")
        }
    }

    private fun getAllDrawings() {
        val token = sharedPreferencesService.getItem(Constants.STORAGE_KEY.TOKEN)
        galleryView.findViewById<TextView>(R.id.loadingDrawingsText).visibility = View.VISIBLE

        drawingRepo.getAllDrawings(token).observe(viewLifecycleOwner, {
            // some error occurred during HTTP request
            if (it.isError as Boolean) {
                return@observe
            }

            drawings = it.data as List<DrawingModel.Drawing>
            galleryView.findViewById<TextView>(R.id.loadingDrawingsText).visibility = View.GONE
            renderDrawings()
        })
    }

    // display all existing drawings in menu
    private fun renderDrawings() {
        val recyclerView = galleryView.findViewById<RecyclerView>(R.id.drawingMenuRecyclerView)
        val drawingsMenu: ArrayList<DrawingMenuData> = arrayListOf()

        // convert src to bitmap for each drawing
        drawings.forEach { drawing ->
            val bitmap: Bitmap? = ImageConvertor(requireContext()).base64ToBitmap(drawing.dataUri)

            if (bitmap != null) {
                drawingsMenu.add(DrawingMenuData(drawing._id, bitmap))
            }
        }

        recyclerView.layoutManager = GridLayoutManager(requireContext(), NB_ROWS)
        recyclerView.adapter = DrawingMenuRecyclerAdapter(drawingsMenu)
    }
}