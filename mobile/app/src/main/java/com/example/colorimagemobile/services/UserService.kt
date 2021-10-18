package com.example.colorimagemobile.services

import com.example.colorimagemobile.models.UserModel

// Singleton User object which is accessible globally
object UserService {
    private lateinit var info: UserModel.AllInfo

    fun setUserInfo(newUserInfo: UserModel.AllInfo) {
        this.info = newUserInfo
    }

    fun getUserInfo(): UserModel.AllInfo {
        return this.info
    }

    fun isNull(): Boolean {
        return !::info.isInitialized
    }
}