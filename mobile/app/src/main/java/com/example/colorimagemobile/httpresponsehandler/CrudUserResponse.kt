package com.example.colorimagemobile.httpresponsehandler

import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.services.UserService

class CrudUserResponse {
    fun handleGetUser(response: DataWrapper<UserModel.AllInfo>) {
        UserService.setUserInfo(response.data as UserModel.AllInfo)
    }
    fun handleGetAllUser(response: DataWrapper<List<UserModel.AllInfo>>) {
        UserService.setAllUserInfo(response.data as List<UserModel.AllInfo>)
    }
}