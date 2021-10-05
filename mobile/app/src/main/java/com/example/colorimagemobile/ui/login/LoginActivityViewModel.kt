package com.example.colorimagemobile.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.LoginResponseModel
import com.example.colorimagemobile.models.User
import com.example.colorimagemobile.repositories.AuthRepository

class LoginActivityViewModel: ViewModel() {

    private val loginResponseLiveData: MutableLiveData<DataWrapper<LoginResponseModel>>
    private val authRepository: AuthRepository

    init {
        loginResponseLiveData = MutableLiveData()
        authRepository = AuthRepository()
    }

    fun getLoginResponseLiveData(): LiveData<DataWrapper<LoginResponseModel>> {
        return loginResponseLiveData
    }

    fun loginUser(user: User): LiveData<DataWrapper<LoginResponseModel>> {
        return authRepository.loginUser(user)
    }
}