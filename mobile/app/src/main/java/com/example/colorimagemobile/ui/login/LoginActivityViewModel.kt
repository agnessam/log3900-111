package com.example.colorimagemobile.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colorimagemobile.classes.User
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.repositories.AuthRepository

class LoginActivityViewModel: ViewModel() {

    private val HTTPResponseLiveData: MutableLiveData<DataWrapper<HTTPResponseModel>>
    private val authRepository: AuthRepository

    init {
        HTTPResponseLiveData = MutableLiveData()
        authRepository = AuthRepository()
    }

    fun getLoginResponseLiveData(): LiveData<DataWrapper<HTTPResponseModel>> {
        return HTTPResponseLiveData
    }

    fun loginUser(user: User.Login): LiveData<DataWrapper<HTTPResponseModel>> {
        return authRepository.loginUser(user)
    }
}