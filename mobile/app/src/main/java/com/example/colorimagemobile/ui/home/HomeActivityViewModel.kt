package com.example.colorimagemobile.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.User
import com.example.colorimagemobile.repositories.AuthRepository

class HomeActivityViewModel : ViewModel() {

    private val authRepository: AuthRepository

    init {
        authRepository = AuthRepository()
    }

    fun logoutUser(user: User): LiveData<DataWrapper<HTTPResponseModel>> {
        return authRepository.logoutUser(user)
    }
}