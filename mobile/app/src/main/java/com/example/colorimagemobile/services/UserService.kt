package com.example.colorimagemobile.services

import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.utils.Constants
import java.time.LocalDateTime

// Singleton User object which is accessible globally

object UserService {
    private lateinit var info: UserModel.AllInfo
    private  var lastLogin: String = "Has never login"
    private  var lastLogout: String = "Has never logout"
    private  var date : ArrayList<String> = arrayListOf("","")

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
        }
        else if (logType == Constants.LAST_LOGOUT_DATE){
            this.lastLogout = (LocalDateTime.now()).toString();
        }

        this.date = arrayListOf(this.lastLogin, this.lastLogout)

    }

    // get login and logout history
    fun getLogHistory(): ArrayList<String>{
        return this.date
    }

}