package com.example.colorimagemobile.ui.home.fragments.museum

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.MuseumPostModel
import com.example.colorimagemobile.repositories.MuseumRepository

class MuseumFragment : Fragment(R.layout.fragment_museum) {

    private lateinit var myView: View
    private lateinit var posts: ArrayList<MuseumPostModel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view
        getAllDrawings()
    }

    private fun getAllDrawings() {
        MuseumRepository().getAllPosts().observe(viewLifecycleOwner, {
            if (it.isError as Boolean) { return@observe }

            posts = it.data as ArrayList<MuseumPostModel>
        })
    }
}