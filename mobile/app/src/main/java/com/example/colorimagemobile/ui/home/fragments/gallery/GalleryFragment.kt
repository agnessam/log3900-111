package com.example.colorimagemobile.ui.home.fragments.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.utils.Constants

class GalleryFragment : Fragment(R.layout.fragment_gallery) {
    private lateinit var sharedPreferencesService: SharedPreferencesService
    private val mainGalleryFragmentID = R.id.main_gallery_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // remove roomID just to be sure
        sharedPreferencesService = SharedPreferencesService(requireContext())
        sharedPreferencesService.removeItem(Constants.STORAGE_KEY.DRAWING_ROOM_ID)

        // go to drawing page
        MyFragmentManager(requireActivity()).open(mainGalleryFragmentID, GalleryMenuFragment())
    }
}