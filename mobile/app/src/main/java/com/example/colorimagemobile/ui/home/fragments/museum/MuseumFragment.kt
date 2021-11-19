package com.example.colorimagemobile.ui.home.fragments.museum

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.MuseumPostRecyclerAdapter
import com.example.colorimagemobile.models.MuseumPostModel
import com.example.colorimagemobile.repositories.MuseumRepository
import com.example.colorimagemobile.services.museum.MuseumPostService
import kotlinx.android.synthetic.main.fragment_user_profile_history.*

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

            for (i in 1..5) {
                posts.add(posts[0])
            }

            MuseumPostService.setPosts(posts)

            val recyclerView = myView.findViewById<RecyclerView>(R.id.museumPostsRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = MuseumPostRecyclerAdapter()
        })
    }
}