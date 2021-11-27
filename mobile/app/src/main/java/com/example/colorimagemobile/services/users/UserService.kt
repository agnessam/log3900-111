package com.example.colorimagemobile.services.users

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.example.colorimagemobile.models.AvatarModel
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

    init {
        updateProfileData =UserModel.UpdateUser(null,null,null)
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

    fun updateUserAfterUpdate(currentdata: UserModel.UpdateUser){
        if (!currentdata.username.isNullOrEmpty()){
            info.username = currentdata.username!!
        }
        if (!currentdata.description.isNullOrEmpty()){
            info.description = currentdata.description!!
        }

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
    }



}