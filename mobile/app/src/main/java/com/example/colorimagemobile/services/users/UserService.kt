package com.example.colorimagemobile.services.users

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.models.AvatarModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants

// Singleton User object which is accessible globally

object UserService {
    private lateinit var info: UserModel.AllInfo
    private var token : String =Constants.EMPTY_STRING
    private var updateProfileData : UserModel.UpdateUser
    private lateinit var allUserInfo : ArrayList<UserModel.AllInfo>
    private var actualNbFollowers : Int = 0
    private var currentUserFollowerList : ArrayList<String> = arrayListOf()
    private var currentUserFollowingList : ArrayList<String> = arrayListOf()
    private var DataForMyFollowersList : ArrayList<UserModel.AllInfo>
    private var DataForFollowingList : ArrayList<UserModel.AllInfo>
    private var userPositionForMenuNavigation: Int? = null

    init {
        updateProfileData =UserModel.UpdateUser(null,null,null)
        DataForMyFollowersList = arrayListOf()
        DataForFollowingList = arrayListOf()
    }

    fun setAllUserInfo(allInfo:ArrayList<UserModel.AllInfo>){
        allUserInfo = allInfo
    }

    fun getAllUserInfo() : ArrayList<UserModel.AllInfo> {
        return allUserInfo
    }

    fun setNewProfileData (newValues: UserModel.UpdateUser){
        updateProfileData = newValues
    }

    fun getNewProfileData(): UserModel.UpdateUser{
        return updateProfileData
    }

    fun setUserInfo(newUserInfo: UserModel.AllInfo) {
        info = newUserInfo
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

    fun updateUserByPosition(position: Int, newFollower: UserModel.AllInfo) {
        allUserInfo[position] = newFollower
    }

    fun removeUserFromFollowing(position: Int) {
        allUserInfo[position].following = allUserInfo[position].following.filter { following -> following != getUserInfo()._id } as ArrayList<String>
    }

    fun isAlreadyFollower(position: Int): Boolean {
        return allUserInfo[position].followers.contains(getUserInfo()._id)
    }

    fun isCurrentUser(position: Int): Boolean {
        return allUserInfo[position]._id.contains(getUserInfo()._id)
    }

    fun isDescriptionNullOrBlank(position: Int): Boolean {
        return allUserInfo[position].description.isNullOrBlank()
    }

    fun updateUserAfterUpdate(currentdata: UserModel.UpdateUser){
        if (!currentdata.username.isNullOrEmpty()){
            info.username = currentdata.username!!
        }
        if (!currentdata.description.isNullOrEmpty()){
            info.description = currentdata.description!!
        }

    }
    fun setCurrentNbFollowers(currentValue: Int){
        actualNbFollowers = currentValue
    }

    fun getCurrentNbFollower(): Int{
        return actualNbFollowers
    }

    fun followUser(position: Int, context: Context) {
        val user = getUser(position)
        UserRepository().followUser(user._id).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                CommonFun.printToast(context, it.message!!)
                return@observe
            }

            val followedUser = it.data as UserModel.AllInfo

            updateUserByPosition(position, followedUser)
            UserAdapterService.getUserMenuAdapter().notifyItemChanged(position)
        })

        actualNbFollowers++
    }

    fun unfollowUser(position: Int, context: Context) {
        val user = getUser(position)
        UserRepository().unfollowUser(user._id).observe(context as LifecycleOwner, {
            if (it.isError as Boolean) {
                CommonFun.printToast(context, it.message!!)
                return@observe
            }

            removeUserFromFollowing(position)
            UserAdapterService.getUserMenuAdapter().notifyItemChanged(position)
        })

        actualNbFollowers--
    }

    fun setUserPosition(position : Int?){
        userPositionForMenuNavigation = position
    }

    fun getUserPosition(): Int?{
        return userPositionForMenuNavigation
    }

    fun setRecyclerDataForFollowers(){

        if ( userPositionForMenuNavigation ==0) {
            currentUserFollowerList = allUserInfo[getUserMePosition()].followers
        } else {
            currentUserFollowerList = allUserInfo[userPositionForMenuNavigation!!].followers
        }

        if(currentUserFollowerList.size!=0){
            for (indice in currentUserFollowerList.indices){
                allUserInfo.find { user -> user._id == currentUserFollowerList[indice]}
                    ?.let { DataForMyFollowersList.add(it) }
            }
        }

    }

    fun setRecyclerDataForFollowing(){

        if ( userPositionForMenuNavigation ==0) {
            currentUserFollowingList = allUserInfo[getUserMePosition()].following
        } else {
            currentUserFollowingList = allUserInfo[userPositionForMenuNavigation!!].following
        }

        if(currentUserFollowingList.size!=0){
            for (indice in currentUserFollowingList.indices){
                allUserInfo.find { user -> user._id == currentUserFollowingList[indice]}
                    ?.let { DataForFollowingList.add(it) }
            }
        }
    }


    fun getRecyclerDataForMyFollowers():ArrayList<UserModel.AllInfo>{
        return DataForMyFollowersList
    }

    fun getRecyclerDataForFollowingList():ArrayList<UserModel.AllInfo>{
        return DataForFollowingList
    }

    fun getUserMePosition(): Int {
        val position = allUserInfo.indexOf(allUserInfo.find { user -> user._id == info._id })
        return  position
    }
}