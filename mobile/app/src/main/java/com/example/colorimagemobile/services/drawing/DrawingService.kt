package com.example.colorimagemobile.services.drawing

import android.content.Context
import android.graphics.Bitmap
import com.example.colorimagemobile.classes.ImageConvertor
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.MuseumPostModel
import com.example.colorimagemobile.models.OwnerModel
import com.example.colorimagemobile.models.PrivacyLevel
import com.example.colorimagemobile.models.recyclerAdapters.DrawingMenuData
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun

object DrawingService {

    private var allDrawings: List<DrawingModel.Drawing> = arrayListOf()
    private var currentDrawingID: String? = null
    private var userCollaborationDrawings: List<DrawingModel.Drawing> = arrayListOf()

    fun setCurrentDrawingID(drawingId: String?) {
        currentDrawingID = drawingId
    }

    fun getCurrentDrawingID(): String? {
        return currentDrawingID
    }

    fun setAllDrawings(newDrawings: List<DrawingModel.Drawing>) {
        allDrawings = newDrawings
    }

    fun getDrawingById(): DrawingModel.Drawing {
        return allDrawings.find { drawing -> drawing._id == currentDrawingID!! }!!
    }

    fun filterDrawingsByPrivacy(drawings: ArrayList<DrawingModel.Drawing>): ArrayList<DrawingModel.Drawing> {
        val filteredDrawings = arrayListOf<DrawingModel.Drawing>()

        drawings.forEach { drawing ->
            // if drawing is public or protected -> show
            if (drawing.privacyLevel == PrivacyLevel.PUBLIC.toString() || drawing.privacyLevel == PrivacyLevel.PROTECTED.toString() ) {
                filteredDrawings.add(drawing)
            }

            if (drawing.privacyLevel == PrivacyLevel.PRIVATE.toString()) {
                // if drawing is private and we are the owner -> show
                if (drawing.ownerModel == OwnerModel.USER.toString() && drawing.owner._id == UserService.getUserInfo()._id) {
                    filteredDrawings.add(drawing)
                }

                // if drawing is private and the ownerModel is Teams, check if user is included in teams
                if (drawing.ownerModel == OwnerModel.TEAM.toString()) {
                    if (checkIfUserIsInTeam(drawing.owner._id) != null) filteredDrawings.add(drawing)
                }
            }
        }

        return filteredDrawings
    }

    fun checkIfUserIsInTeam(owner: String): String? {
        return UserService.getUserInfo().teams.find { teamId -> teamId == owner }
    }

    // convert src to bitmap for each drawing
    fun getDrawingsBitmap(context: Context, drawings: List<DrawingModel.Drawing>): ArrayList<DrawingMenuData> {
        val imageConvertor = ImageConvertor(context)
        val drawingsMenu: ArrayList<DrawingMenuData> = arrayListOf()

        drawings.forEach { drawing ->
            val bitmap: Bitmap? = imageConvertor.renderBase64ToBitmap(drawing.dataUri)
            val svgString = imageConvertor.getSvgAsString(drawing.dataUri)

            if (bitmap != null) {
                drawingsMenu.add(DrawingMenuData(drawing, bitmap, svgString))
            }
        }

        return drawingsMenu
    }

    fun updateDrawingFromMenu(drawingMenuData: DrawingMenuData, updatedDrawing: DrawingModel.UpdateDrawing): DrawingMenuData {
        drawingMenuData.drawing.name = updatedDrawing.name
        drawingMenuData.drawing.password = updatedDrawing.password
        drawingMenuData.drawing.privacyLevel = updatedDrawing.privacyLevel

        return drawingMenuData
    }

    fun isOwner(drawing: DrawingModel.Drawing): Boolean {
        if (drawing.ownerModel == OwnerModel.USER.toString()) {
            if (drawing.owner._id == UserService.getUserInfo()._id) {
                return true
            }
        }

        // drawing belongs to a group ==> owner is teamId
        if (drawing.ownerModel == OwnerModel.TEAM.toString()) {
            if (checkIfUserIsInTeam(drawing.owner._id) != null) return true
        }

        return false
    }

    fun getDrawing(position: Int): DrawingModel.Drawing{
        return  allDrawings[position]
    }
//    fun getCollaborationDrawingObject(): List<DrawingModel.Drawing> {
//
//        when(UserService.getUserInfo().collaborationHistory.size){
//            0->{}
//            1->{val firstDrawing = allDrawings.find { drawing -> drawing._id == UserService.getIdCollaborationToShow()[0] }!!
//                userCollaborationDrawings = arrayListOf(firstDrawing)}
//            2->{ val firstDrawing = allDrawings.find { drawing -> drawing._id == UserService.getIdCollaborationToShow()[0] }!!
//                val secondDrawing = allDrawings.find { drawing -> drawing._id == UserService.getIdCollaborationToShow()[1] }!!
//                userCollaborationDrawings = arrayListOf(firstDrawing,secondDrawing)
//            }
//            else->{
//                val firstDrawing = allDrawings.find { drawing -> drawing._id == UserService.getIdCollaborationToShow()[0] }!!
//                val secondDrawing = allDrawings.find { drawing -> drawing._id == UserService.getIdCollaborationToShow()[1] }!!
//                val thirdDrawing = allDrawings.find { drawing -> drawing._id == UserService.getIdCollaborationToShow()[2] }!!
//                userCollaborationDrawings = arrayListOf(firstDrawing,secondDrawing,thirdDrawing )
//            }
//        }

//
//
//        CommonFun.printMsg("all collaborationdrawing " + userCollaborationDrawings)
//
//        return userCollaborationDrawings
//
//    }
}