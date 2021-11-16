package com.example.colorimagemobile.services

import com.example.colorimagemobile.models.AvatarModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.utils.Constants

// Singleton User object which is accessible globally

object UserService {
    private lateinit var info: UserModel.AllInfo
    private var token : String =Constants.EMPTY_STRING
    private var updateProfileData : UserModel.UpdateUser
    private lateinit var allUserInfo : List<UserModel.AllInfo>

    init {
        updateProfileData =UserModel.UpdateUser(
            Constants.EMPTY_STRING,
            Constants.EMPTY_STRING,
            Constants.EMPTY_STRING,
            AvatarModel.AllInfo(
                Constants.EMPTY_STRING,
                Constants.EMPTY_STRING,
                true
            )
        )
    }

    fun setAllUserInfo(allInfo:List<UserModel.AllInfo>){
        this.allUserInfo = allInfo
    }
    fun getAllUserInfo() : List<UserModel.AllInfo> {
        return this.allUserInfo
    }

    fun setNewProfileData (newValues: UserModel.UpdateUser){
        this.updateProfileData = newValues
    }

    fun getNewProfileData(): UserModel.UpdateUser{
        return this.updateProfileData
    }

    fun setUserInfo(newUserInfo: UserModel.AllInfo) {
        this.info = newUserInfo
    }

    fun getUserInfo(): UserModel.AllInfo {
        return this.info
    }

    fun isNull(): Boolean {
        return !::info.isInitialized
    }

    fun setToken(token:String){
        this.token = token
    }

    fun getToken(): String{

        return this.token
    }


}