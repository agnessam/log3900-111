package com.example.colorimagemobile.services

import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.utils.Constants
import java.time.LocalDateTime

// Singleton User object which is accessible globally

object UserService {
    private lateinit var info: UserModel.AllInfo
    private lateinit var lastLogin: String
    private lateinit var lastLogout: String
    private  var date : ArrayList<String> = arrayListOf(Constants.EMPTY_STRING,Constants.EMPTY_STRING)
    private var token : String =Constants.EMPTY_STRING
    private var updateProfileData : UserModel.UpdateUser = UserModel.UpdateUser(Constants.EMPTY_STRING,Constants.EMPTY_STRING,Constants.EMPTY_STRING)
    private lateinit var allUserInfo : List<UserModel.AllInfo>
    private lateinit var logHistory : UserModel.UpdateLogHistory

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

    // set login and logout history
    fun setLogHistory(logType : String){

        if (logType == Constants.LAST_LOGIN_DATE){
            this.lastLogin = (LocalDateTime.now()).toString()
            this.date[0] = this.lastLogin
        }
        else if (logType == Constants.LAST_LOGOUT_DATE){
            this.lastLogout = (LocalDateTime.now()).toString();
            this.date[1] = this.lastLogout
        }

        this.logHistory = UserModel.UpdateLogHistory(this.date[0], this.date[1])
    }


    // get login and logout history
    fun getLogHistory(): UserModel.UpdateLogHistory{
        return this.logHistory
    }

    fun setToken(token:String){
        this.token = token
    }

    fun getToken(): String{

        return this.token
    }

}