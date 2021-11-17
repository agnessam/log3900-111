package com.example.colorimagemobile.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.repositories.AuthRepository
import com.example.colorimagemobile.repositories.TextChannelRepository
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.UserService

class HomeActivityViewModel : ViewModel() {

    private val authRepository: AuthRepository
    private val userRepository: UserRepository

    init {
        authRepository = AuthRepository()
        userRepository = UserRepository()
    }

    fun getUserByToken(token: String): LiveData<DataWrapper<HTTPResponseModel.UserResponse>> {
        return userRepository.getUserByToken(token)
    }

    fun logoutUser(user: UserModel.Logout): LiveData<DataWrapper<HTTPResponseModel>> {
        return authRepository.logoutUser(user)
    }
}