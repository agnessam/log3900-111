package com.example.colorimagemobile.ui.home.fragments.accountParameter


import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.colorimagemobile.R
import com.example.colorimagemobile.adapter.CollaborationHistoryRecyclerAdapter
import com.example.colorimagemobile.adapter.DrawingMenuRecyclerAdapter
import com.example.colorimagemobile.classes.MyFragmentManager
import com.example.colorimagemobile.models.CollaborationHistory
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.repositories.DrawingRepository
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.ui.home.fragments.gallery.GalleryDrawingFragment
import com.example.colorimagemobile.utils.CommonFun
import kotlinx.android.synthetic.main.card_collaboration_history.*
import kotlinx.android.synthetic.main.fragment_user_profile_history.*


class HistoryFragment : Fragment() {

    private var collabDrawingObject : List<DrawingModel.Drawing> = arrayListOf()
    private var bitmapOfdrawingToOpen : ArrayList<DrawingMenuData> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private var adapter : RecyclerView.Adapter<CollaborationHistoryRecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserService.setCollaborationHistoryToshow()
        DrawingService.setCollaborationDrawingObject()
        collabDrawingObject = DrawingService.getCollaborationDrawingObject()
        bitmapOfdrawingToOpen = DrawingService.getDrawingsBitmap(requireContext(), collabDrawingObject)
        DrawingService.setCollabHistoryDrawingsBitmap(bitmapOfdrawingToOpen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inf = inflater.inflate(R.layout.fragment_user_profile_history, container, false)

        recyclerView = inf.findViewById(R.id.collabHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        adapter = CollaborationHistoryRecyclerAdapter()
//        recyclerView.adapter = adapter

        return inf
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter  = DrawingMenuRecyclerAdapter(requireActivity(), bitmapOfdrawingToOpen, R.id.usersMenuFrameLayout) { updatedDrawing, pos -> updateDrawing(updatedDrawing, pos) }

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

