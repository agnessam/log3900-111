package com.example.colorimagemobile.services.users

import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.utils.Constants

// Singleton User object which is accessible globally

object UserService {
    private lateinit var info: UserModel.AllInfo
    private lateinit var lastLogin: String
    private lateinit var lastLogout: String
    private  var date : ArrayList<String> = arrayListOf(Constants.EMPTY_STRING,Constants.EMPTY_STRING)
    private var token : String =Constants.EMPTY_STRING
    private var updateProfileData : UserModel.UpdateUser = UserModel.UpdateUser(Constants.EMPTY_STRING,Constants.EMPTY_STRING,Constants.EMPTY_STRING)
    private lateinit var AllUserInfo : List<UserModel.AllInfo>
    private lateinit var logHistory : UserModel.UpdateLogHistory

    fun setAllUserInfo(allInfo:List<UserModel.AllInfo>){
        AllUserInfo = allInfo
    }

    fun getAllUserInfo() : List<UserModel.AllInfo> {
        return AllUserInfo
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

}