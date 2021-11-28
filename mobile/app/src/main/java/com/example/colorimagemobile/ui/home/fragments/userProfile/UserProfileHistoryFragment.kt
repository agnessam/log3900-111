package com.example.colorimagemobile.ui.home.fragments.userProfile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.CollaborationHistory
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.repositories.DrawingRepository
import com.example.colorimagemobile.services.CollaborationHistory.CollabHistoryAdapterService
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.Constants
import kotlinx.android.synthetic.main.fragment_user_profile_history.*


class UserProfileHistoryFragment : Fragment(R.layout.fragment_user_profile_history) {

//    val drawingMenus: ArrayList<DrawingMenuData> = drawings
    private lateinit var myView : View
    private lateinit var drawings: ArrayList<DrawingModel.Drawing>
    private var drawingPosition: Int? = null
    private lateinit var currentDrawing: DrawingModel.Drawing
    private lateinit var collabHistoryToShow : ArrayList<CollaborationHistory.drawingHistory>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            drawingPosition = it.getInt(Constants.DRAWINGS.CURRENT_DRAWING_ID_KEY)
            currentDrawing = DrawingService.getDrawing(drawingPosition!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        MyFragmentManager(requireActivity()).hideBackButton()
        collabHistoryToShow = UserService.getCollaborationToShow()
        getAllDrawings()
        setListeners()
    }

    private fun setListeners() {
        // set value for login and logout
        myView.findViewById<TextView>(R.id.userLastLogin).text = UserService.getUserInfo().lastLogin.toString()
        myView.findViewById<TextView>(R.id.userlastLogout).text = UserService.getUserInfo().lastLogout.toString()
    }

    private fun getAllDrawings() {
        val token = UserService.getToken()

        DrawingRepository().getAllDrawings(token).observe(viewLifecycleOwner, {
            // some error occurred during HTTP request
            if (it.isError as Boolean) {
                return@observe
            }

            drawings = it.data  as ArrayList<DrawingModel.Drawing>
            DrawingService.setAllDrawings(drawings)
            val objectDrawing = DrawingService.getCollaborationDrawingObject()

            val recyclerView = myView.findViewById<RecyclerView>(R.id.collabHistoryRecyclerView)
            recyclerView.layoutManager = GridLayoutManager(requireContext(), Constants.NB_DATA_ROWS)

            val adapter = CollabHistoryAdapterService.createAdapter(requireActivity(), R.layout.card_collaboration_history, R.id.collabHistoryRecyclerView)
            recyclerView.adapter = adapter
            CollabHistoryAdapterService.setAdapter(adapter)

//            renderDrawings()
        })
    }


//    private fun openDrawing(position: Int, context: Context) {
//        DrawingObjectManager.createDrawableObjects(drawingMenus[position].svgString)
//
//        DrawingService.setCurrentDrawingID(drawingMenus[position].drawing._id)
//        MyFragmentManager(context as FragmentActivity).open(destination, GalleryDrawingFragment())
//        CanvasUpdateService.invalidate()
//    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        layoutManager = LinearLayoutManager(context)
//        collabHistoryRecyclerView.layoutManager = layoutManager
//        adapter = CollaborationHistoryRecyclerAdapter()
//        collabHistoryRecyclerView.adapter = adapter
//    }


}

