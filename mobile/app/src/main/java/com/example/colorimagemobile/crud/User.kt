package com.example.colorimagemobile.crud

import androidx.lifecycle.LiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.repositories.UserRepository

class User {

    private val userRepository: UserRepository = UserRepository()

    fun getUserById(token: String,id:String): LiveData<DataWrapper<UserModel.AllInfo>> {
        return userRepository.getUserById(token,id)
    }
    private fun deleteUser(token: String, id: String): LiveData<DataWrapper<HTTPResponseModel.UserResponse>> {
        return userRepository.deleteUserById(token, id)
    }
    private fun getAllUser(token: String): LiveData<DataWrapper<List<UserModel.AllInfo>>> {
        return userRepository.getAllUser(token)
    }



}