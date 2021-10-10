package com.example.colorimagemobile.classes

import com.example.colorimagemobile.models.UserModel

// Singleton User object which is accessible globally
object User {
    private lateinit var info: UserModel.AllInfo

    fun setUserInfo(newUserInfo: UserModel.AllInfo) {
        this.info = newUserInfo
    }

    fun getUserInfo(): UserModel.AllInfo {
        return this.info
    }
}