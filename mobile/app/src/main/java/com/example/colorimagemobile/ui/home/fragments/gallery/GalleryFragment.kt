package com.example.colorimagemobile.ui.home.fragments.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyFragmentManager

class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val mainGalleryFragmentID = R.id.main_gallery_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // go to drawing page
        MyFragmentManager(requireActivity()).open(mainGalleryFragmentID, GalleryMenuFragment())
    }
}