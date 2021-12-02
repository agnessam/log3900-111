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
    private var userStatistics : UserModel.Statistics


    init {
        updateProfileData =UserModel.UpdateUser(null,null,null)
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

    fun updateUserFollowers(userId: String, newFollower: UserModel.AllInfo) {
        var user = allUserInfo.find { user -> user._id == userId }
        user = newFollower
    }

    fun removeUserFromFollowing(userId: String) {
        val user = allUserInfo.find {user -> user._id == userId}
        user?.following = user?.following!!.filter { following -> following != getUserInfo()._id } as ArrayList<String>
    }

    fun updateUserAfterUpdate(currentdata: UserModel.UpdateUser){
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

    fun setStat(stat: UserModel.Statistics){
        userStatistics = stat
    }
    fun getStat(): UserModel.Statistics{
        return userStatistics
    }

}