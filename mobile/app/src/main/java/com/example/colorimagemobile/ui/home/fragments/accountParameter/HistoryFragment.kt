package com.example.colorimagemobile.ui.home.fragments.accountParameter


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.CollaborationHistoryRecyclerAdapter
import com.example.colorimagemobile.classes.DateFormatter
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.repositories.DrawingRepository
import com.example.colorimagemobile.services.SharedPreferencesService
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.Constants


class HistoryFragment : Fragment(R.layout.fragment_user_profile_history) {

    private var collabDrawingObject : List<DrawingModel.Drawing> = arrayListOf()
    private var bitmapOfdrawingToOpen : ArrayList<DrawingMenuData> = arrayListOf()
    private lateinit var sharedPreferencesService: SharedPreferencesService


    //    private var drawingPosition: Int? = null
//    private lateinit var currentDrawing: DrawingModel.Drawing
    private lateinit var recyclerView: RecyclerView
    private lateinit var myView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            drawingPosition = it.getInt(Constants.DRAWINGS.CURRENT_DRAWING_ID_KEY)
//            currentDrawing = DrawingService.getDrawing(drawingPosition!!)
//        }
        UserService.setCollaborationHistoryToshow()
        DrawingService.setCollaborationDrawingObject()
        collabDrawingObject = DrawingService.getCollaborationDrawingObject()
        bitmapOfdrawingToOpen = DrawingService.getDrawingsBitmap(requireContext(), collabDrawingObject)
        DrawingService.setCollabHistoryDrawingsBitmap(bitmapOfdrawingToOpen)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferencesService = context?.let { SharedPreferencesService(it) }!!

        myView = view

        // SET LOGIN AND LOGOUT VALUE
        when(UserService.getUserInfo().lastLogin.toString()){
            Constants.EMPTY_STRING -> {}
            Constants.NULL -> {}
            else->{
                val loginDate = DateFormatter.getDate(UserService.getUserInfo().lastLogin.toString())
                myView.findViewById<TextView>(R.id.userLastLogin).text = loginDate}
        }
        when(UserService.getUserInfo().lastLogout){
            null -> {}
            else->{
                val logoutDate = UserService.getUserInfo().lastLogout.toString()
                myView.findViewById<TextView>(R.id.userlastLogout).text = logoutDate}
        }

        recyclerView = myView.findViewById(R.id.collabHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = CollaborationHistoryRecyclerAdapter(bitmapOfdrawingToOpen, R.id.collaborationFrameLayout) { updatedDrawing, pos -> updateDrawing(updatedDrawing, pos) }


    }

    private fun updateDrawing(updatedDrawing: DrawingModel.UpdateDrawing, position: Int) {
        DrawingRepository().updateDrawing(bitmapOfdrawingToOpen[position].drawing._id!!, updatedDrawing).observe(viewLifecycleOwner, {
            if (it.isError as Boolean) {
                CommonFun.printToast(requireActivity(), it.message!!)
                return@observe
            }

            bitmapOfdrawingToOpen[position] = DrawingService.updateDrawingFromMenu(bitmapOfdrawingToOpen[position], updatedDrawing)
            recyclerView.adapter?.notifyItemChanged(position)
        })
    }

    private fun getAllDrawings() {
        val token = sharedPreferencesService.getItem(Constants.STORAGE_KEY.TOKEN)
        galleryView.findViewById<TextView>(R.id.loadingDrawingsText).visibility = View.VISIBLE

        drawingRepo.getAllDrawings(token).observe(viewLifecycleOwner, {
            // some error occurred during HTTP request
            if (it.isError as Boolean) {
                return@observe
            }

            drawings = DrawingService.filterDrawingsByPrivacy(it.data as ArrayList<DrawingModel.Drawing>)
            DrawingService.setAllDrawings(drawings)
            galleryView.findViewById<TextView>(R.id.loadingDrawingsText).visibility = View.GONE
            renderDrawings()
        })
    }

}

