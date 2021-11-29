package com.example.colorimagemobile.services.users

import com.example.colorimagemobile.models.AvatarModel
import com.example.colorimagemobile.models.CollaborationHistory
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants
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
    private var currentUserFollowerList : ArrayList<String> = arrayListOf()
    private var currentUserFollowingList : ArrayList<String> = arrayListOf()
    private var DataForMyFollowersList : ArrayList<UserModel.AllInfo>
    private var DataForFollowingList : ArrayList<UserModel.AllInfo>
    private var userPositionForMenuNavigation: Int? = null

    init {
        collaborationHistoryToShow = arrayListOf()
        temporaryDescription = Constants.EMPTY_STRING
        temporaryEditUsername = Constants.EMPTY_STRING
        DataForMyFollowersList = arrayListOf()
        DataForFollowingList = arrayListOf()
    }

    fun setUserPosition(position : Int?){
        userPositionForMenuNavigation = position
    }

    fun getUserPosition(): Int?{
        return userPositionForMenuNavigation
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

    fun setRecyclerDataForFollowers(){
        printMsg("userPosition "+ userPositionForMenuNavigation)
        printMsg("All user Info "+ allUserInfo)


//        if (userPositionForMenuNavigation !=null ){
//
//        }

        if ( userPositionForMenuNavigation ==0) {
            printMsg("Position inside formmenunav != o "+getUserMePosition())
            currentUserFollowerList = allUserInfo[getUserMePosition()].followers
        } else {
            printMsg("inside else currentlist folowers")
            currentUserFollowerList = allUserInfo[userPositionForMenuNavigation!!].followers
        }

        if(currentUserFollowerList.size!=0){
            for (indice in currentUserFollowerList.indices){
                DataForMyFollowersList= allUserInfo.find { user -> user._id == currentUserFollowerList[indice]}
                    ?.let { arrayListOf(it) }!!
                printMsg("followers ${currentUserFollowerList[indice]} = ${DataForMyFollowersList}")
            }
        }

    }

    fun setRecyclerDataForFollowing(){
        currentUserFollowingList = allUserInfo[userPositionForMenuNavigation!!].following
        if(currentUserFollowingList.size!=0){
            for (indice in currentUserFollowingList.indices){
                DataForFollowingList= allUserInfo.find { user -> user._id == currentUserFollowingList[indice]}
                    ?.let { arrayListOf(it) }!!

                printMsg("followers ${currentUserFollowingList[indice]} = ${DataForFollowingList}")
            }
        }

    }

    fun getRecyclerDataForMyFollowers():ArrayList<UserModel.AllInfo>{
        printMsg("followers list finale "+ DataForMyFollowersList)
        return DataForMyFollowersList
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