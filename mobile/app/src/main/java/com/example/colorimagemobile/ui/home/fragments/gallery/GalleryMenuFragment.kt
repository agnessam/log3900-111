package com.example.colorimagemobile.ui.home.fragments.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.repositories.DrawingRepository
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.utils.Constants
import java.util.*
import android.graphics.Bitmap
import com.example.colorimagemobile.classes.ImageConvertor

class GalleryMenuFragment : Fragment(R.layout.fragment_gallery_menu) {
    private lateinit var drawingRepo: DrawingRepository
    private lateinit var sharedPreferencesService: SharedPreferencesService
    private lateinit var drawings: List<DrawingModel.AllDrawings>
    private lateinit var galleryView: ConstraintLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawingRepo = DrawingRepository()
        sharedPreferencesService = context?.let { SharedPreferencesService(it) }!!
        galleryView = view.findViewById(R.id.galleryMenuView)
        drawings = arrayListOf()

        getAllDrawings()
    }

    private fun getAllDrawings() {
        val token = sharedPreferencesService.getItem(Constants.STORAGE_KEY.TOKEN)

        drawingRepo.getAllDrawings(token).observe(viewLifecycleOwner, {
            // some error occurred during HTTP request
            if (it.isError as Boolean) {
                return@observe
            }

            drawings = it.data as List<DrawingModel.AllDrawings>
            renderDrawings()
        })
    }

    // display all existing drawings in menu
    private fun renderDrawings() {
        val dataUri = drawings[7].dataUri
        val bitmap: Bitmap? = ImageConvertor(requireContext()).base64ToBitmap(dataUri)

        if (bitmap != null) {
            val img = galleryView.findViewById<ImageView>(R.id.image)
            img.setImageBitmap(bitmap)
        }
    }
}