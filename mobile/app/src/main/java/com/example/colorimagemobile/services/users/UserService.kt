package com.example.colorimagemobile.services.users

import com.example.colorimagemobile.models.AvatarModel
import com.example.colorimagemobile.models.CollaborationHistory
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.services.drawing.DrawingService
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

// Singleton User object which is accessible globally

object UserService {
    private lateinit var info: UserModel.AllInfo
    private var token : String =Constants.EMPTY_STRING
    private lateinit var allUserInfo : ArrayList<UserModel.AllInfo>
    private  var collaborationHistoryToShow : ArrayList<CollaborationHistory.drawingHistory>
    private var collaborationHistoryDrawingId: ArrayList<String> = arrayListOf()
    private var temporaryEditUsername : String
    private var temporaryDescription : String

    init {
        collaborationHistoryToShow = arrayListOf()
        temporaryDescription = Constants.EMPTY_STRING
        temporaryEditUsername = Constants.EMPTY_STRING
    }

    fun setAllUserInfo(allInfo:ArrayList<UserModel.AllInfo>){
        allUserInfo = allInfo
    }

    fun getAllUserInfo() : ArrayList<UserModel.AllInfo> {
        return allUserInfo
    }

    fun setUserInfo(newUserInfo: UserModel.AllInfo) {
        info = newUserInfo
        setCollaborationHistoryToshow()
    }

    fun getUserMePosition(): Int {
        val position = allUserInfo.indexOf(allUserInfo.find { user -> user._id == info._id })
        return  position
    }

    fun getUserInfo(): UserModel.AllInfo {
        return info
    }

    fun isNull(): Boolean {
        return !UserService::info.isInitialized
    }

    fun setToken(token:String){
        UserService.token = token
    }

    fun getToken(): String{
        return token
    }

    fun setUserAvatar(avatar: AvatarModel.AllInfo){
     this.info.avatar = avatar
    }

    fun getUser(position: Int): UserModel.AllInfo {
        return allUserInfo[position]
    }

    fun updateMe(currentdata: UserModel.UpdateUser){
        if (!currentdata.username.isNullOrEmpty()){
            info.username = currentdata.username!!
        }
        if (!currentdata.description.isNullOrEmpty()){
            info.description = currentdata.description!!
        }

    }

    fun setCollaborationHistoryToshow(){

        printMsg("Taille collab : "+info.collaborationHistory.size)
        when(info.collaborationHistory.size){

            0->{}
            1 -> {
                collaborationHistoryToShow.add(info.collaborationHistory[0])
                collaborationHistoryDrawingId.add(info.collaborationHistory[0].drawing)
            }
            2 ->{
                collaborationHistoryToShow.add(info.collaborationHistory[0])
                collaborationHistoryToShow.add(info.collaborationHistory[1])
                collaborationHistoryDrawingId.add(info.collaborationHistory[0].drawing)
                collaborationHistoryDrawingId.add(info.collaborationHistory[1].drawing)

            }
            else -> {
                // set collaboration data
                val countLog = info.collaborationHistory.size
                collaborationHistoryToShow.add(info.collaborationHistory[countLog-1])
                collaborationHistoryToShow.add(info.collaborationHistory[countLog-2])
                collaborationHistoryToShow.add(info.collaborationHistory[countLog-3])

                collaborationHistoryDrawingId.add(info.collaborationHistory[countLog-1].drawing)
                collaborationHistoryDrawingId.add(info.collaborationHistory[countLog-2].drawing)
                collaborationHistoryDrawingId.add(info.collaborationHistory[countLog-3].drawing)
            }

        }

    }

    fun getCollaborationToShow() : ArrayList<CollaborationHistory.drawingHistory>{
        return collaborationHistoryToShow
    }

    fun getIdCollaborationToShow() : ArrayList<String>{
        return collaborationHistoryDrawingId
    }

    fun setTemporaryEditUsername(newUsername: String){
        this.temporaryEditUsername = newUsername
    }

    fun getTemporaryEditUsername(): String{
        return this.temporaryEditUsername
    }

    fun setTemporaryDescription(newDescription: String){
        this.temporaryDescription = newDescription
    }

    fun getTemporaryDescription(): String{
        return this.temporaryDescription
    }


}