package com.example.colorimagemobile.services.users

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.models.AvatarModel
import com.example.colorimagemobile.models.CollaborationHistory
import com.example.colorimagemobile.models.Privacy
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.Constants

// Singleton User object which is accessible globally

object UserService {
    private lateinit var info: UserModel.AllInfo
    private var token : String =Constants.EMPTY_STRING
    private var updateProfileData : UserModel.UpdateUser
    private lateinit var allUserInfo : ArrayList<UserModel.AllInfo>
    private var actualNbFollowers : Int = 0
    private var userPositionForMenuNavigation: Int? = null
    private var collaborationHistoryToShow : ArrayList<CollaborationHistory.drawingHistory>
    private var collaborationHistoryDrawingId: ArrayList<String>
    private var temporaryEditUsername : String
    private var temporaryDescription : String
    private var userStatistics : UserModel.Statistics

    init {
        updateProfileData =UserModel.UpdateUser(null,null,null)
        collaborationHistoryToShow = arrayListOf()
        collaborationHistoryDrawingId = arrayListOf()
        temporaryDescription = Constants.EMPTY_STRING
        temporaryEditUsername = Constants.EMPTY_STRING
        userStatistics = UserModel.Statistics(
            Constants.EMPTY_STRING,
            0,0,0,0.00,0.00)
    }

    fun setAllUserInfo(allInfo:ArrayList<UserModel.AllInfo>){
        allUserInfo = allInfo
    }

    fun getAllUserInfo() : ArrayList<UserModel.AllInfo> {
        return allUserInfo
    }

    fun setUserInfo(newUserInfo: UserModel.AllInfo) {
        info = newUserInfo
    }

    fun getUserInfo(): UserModel.AllInfo {
        return info
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

    fun updateUserFollowers(userId: String, newFollower: UserModel.AllInfo) {
        var user = allUserInfo.find { user -> user._id == userId }
        user = newFollower
    }

    fun removeUserFromFollowing(userId: String) {
        val user = allUserInfo.find {user -> user._id == userId}
        user?.following = user?.following!!.filter { following -> following != getUserInfo()._id } as ArrayList<String>
    }

    fun updateMe(currentdata: UserModel.UpdateUser){
        if (!currentdata.username.isNullOrEmpty()){
            info.username = currentdata.username!!
        }
        if (!currentdata.description.isNullOrEmpty()){
            info.description = currentdata.description!!
        }

    }

    fun getCurrentNbFollower(): Int{
        return actualNbFollowers
    }

    fun setCurrentNbFollowers(currentValue: Int){
        actualNbFollowers = currentValue
    }

    fun followUser(userId: String, context: Context) {
        UserRepository().followUser(userId).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                CommonFun.printToast(context, it.message!!)
                return@observe
            }

            val followedUser = it.data as UserModel.AllInfo
            updateUserFollowers(userId, followedUser)
//            UserAdapterService.getUserMenuAdapter().notifyItemChanged(position)
        })

        actualNbFollowers++
    }

    fun unfollowUser(userId: String, context: Context) {
        UserRepository().unfollowUser(userId).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                CommonFun.printToast(context, it.message!!)
                return@observe
            }

            removeUserFromFollowing(userId)
//            UserAdapterService.getUserMenuAdapter().notifyItemChanged(position)
        })

        actualNbFollowers--
    }

    fun setUserPosition(position : Int?){
        userPositionForMenuNavigation = position
    }

    fun getUserPosition(): Int?{
        return userPositionForMenuNavigation
    }

    fun getUserMePosition(): Int {
        val position = allUserInfo.indexOf(allUserInfo.find { user -> user._id == info._id })
        return  position
    }
    fun setCollaborationHistoryToshow() {

        when (info.collaborationHistory!!.size) {

            0 -> {}
            1 -> {
                collaborationHistoryToShow.add(info.collaborationHistory!![0])
                collaborationHistoryDrawingId.add(info.collaborationHistory!![0].drawing)
            }
            2 -> {
                collaborationHistoryToShow.add(info.collaborationHistory!![0])
                collaborationHistoryToShow.add(info.collaborationHistory!![1])
                collaborationHistoryDrawingId.add(info.collaborationHistory!![0].drawing)
                collaborationHistoryDrawingId.add(info.collaborationHistory!![1].drawing)

            }
            else -> {
                // set collaboration data
                val countLog = info.collaborationHistory!!.size
                collaborationHistoryToShow.add(info.collaborationHistory!![countLog - 1])
                collaborationHistoryToShow.add(info.collaborationHistory!![countLog - 2])
                collaborationHistoryToShow.add(info.collaborationHistory!![countLog - 3])

                collaborationHistoryDrawingId.add(info.collaborationHistory!![countLog - 1].drawing)
                collaborationHistoryDrawingId.add(info.collaborationHistory!![countLog - 2].drawing)
                collaborationHistoryDrawingId.add(info.collaborationHistory!![countLog - 3].drawing)
            }

        }

    }

    fun getCollaborationToShow(): ArrayList<CollaborationHistory.drawingHistory> {
        return collaborationHistoryToShow
    }
    fun getIdCollaborationToShow(): ArrayList<String> {
        return collaborationHistoryDrawingId
    }

    fun setStat(stat: UserModel.Statistics){
        userStatistics = stat
    }
    fun getStat(): UserModel.Statistics{
        return userStatistics
    }

    fun updatePrivacySetting(newSetting : Privacy.Setting ){
      this.info.privacySetting = newSetting
    }


}