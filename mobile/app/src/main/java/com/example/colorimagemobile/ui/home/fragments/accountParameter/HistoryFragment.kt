package com.example.colorimagemobile.ui.home.fragments.accountParameter


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.CollaborationHistoryRecyclerAdapter
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.Privacy
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
    private var drawingPosition: Int? = null
    private lateinit var currentDrawing: DrawingModel.Drawing
    private lateinit var recyclerView: RecyclerView
    private lateinit var myView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            drawingPosition = it.getInt(Constants.DRAWINGS.CURRENT_DRAWING_ID_KEY)
            currentDrawing = DrawingService.getDrawing(drawingPosition!!)
        }
        UserService.setCollaborationHistoryToshow()
        DrawingService.setCollaborationDrawingObject()
        collabDrawingObject = DrawingService.getCollaborationDrawingObject()
        bitmapOfdrawingToOpen = DrawingService.getDrawingsBitmap(requireContext(), collabDrawingObject)
        DrawingService.setCollabHistoryDrawingsBitmap(bitmapOfdrawingToOpen)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        when(UserService.getUserInfo().lastLogin.toString()){
            Constants.EMPTY_STRING -> {}
            else->{ myView.findViewById<TextView>(R.id.userLastLogin).text = UserService.getUserInfo().lastLogin.toString()}
        }
        when(UserService.getUserInfo().lastLogout){
            null -> {}
            else->{myView.findViewById<TextView>(R.id.userlastLogout).text = UserService.getUserInfo().lastLogout.toString()}
        }

        recyclerView = myView.findViewById(R.id.collabHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CollaborationHistoryRecyclerAdapter(bitmapOfdrawingToOpen, R.id.collaborationFrameLayout) { updatedDrawing, pos -> updateDrawing(updatedDrawing, pos) }


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

}

