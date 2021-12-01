package com.example.colorimagemobile.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.repositories.AuthRepository
import com.example.colorimagemobile.repositories.TextChannelRepository
import com.example.colorimagemobile.repositories.UserRepository

class LoginActivityViewModel: ViewModel() {

    private val HTTPResponseLiveData: MutableLiveData<DataWrapper<HTTPResponseModel>>
    private val authRepository: AuthRepository

    init {
        HTTPResponseLiveData = MutableLiveData()
        authRepository = AuthRepository()

    }

    fun loginUser(user: UserModel.Login): LiveData<DataWrapper<HTTPResponseModel.LoginResponse>> {
        return authRepository.loginUser(user)
    }
}