package com.example.colorimagemobile.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.DrawingModel
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.repositories.AuthRepository
import com.example.colorimagemobile.repositories.DrawingRepository
import com.example.colorimagemobile.repositories.UserRepository

class HomeActivityViewModel : ViewModel() {

    private val authRepository: AuthRepository
    private val userRepository: UserRepository
    private val drawingRepository: DrawingRepository

    init {
        authRepository = AuthRepository()
        userRepository = UserRepository()
        drawingRepository = DrawingRepository()
    }


    fun logoutUser(user: UserModel.Logout): Boolean {
        return authRepository.logoutUser(user)
    }

    fun saveDrawing(saveDrawing: DrawingModel.SaveDrawing): MutableLiveData<DataWrapper<DrawingModel.CreateDrawing>> {
        return drawingRepository.saveDrawing(saveDrawing)
    }
}