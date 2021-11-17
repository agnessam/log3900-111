package com.example.colorimagemobile.ui.home.fragments.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.colorimagemobile.R
import com.example.colorimagemobile.models.SearchModel
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var queryObject: SearchModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            queryObject = it.getSerializable(Constants.SEARCH.CURRENT_QUERY) as SearchModel
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        printMsg(queryObject.toString())
    }
}