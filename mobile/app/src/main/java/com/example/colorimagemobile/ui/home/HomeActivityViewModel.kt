package com.example.colorimagemobile.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.repositories.AuthRepository
import com.example.colorimagemobile.repositories.ChatChannelRepository
import com.example.colorimagemobile.repositories.UserRepository

class HomeActivityViewModel : ViewModel() {

    private val authRepository: AuthRepository
    private val userRepository: UserRepository
//    private val chatChannelRepository : ChatChannelRepository

    init {
        authRepository = AuthRepository()
        userRepository = UserRepository()
//        chatChannelRepository = ChatChannelRepository()
    }

    fun getUserByToken(token: String): LiveData<DataWrapper<HTTPResponseModel.GetUser>> {
        return userRepository.getUserByToken(token)
    }

    fun logoutUser(user: UserModel.Logout): LiveData<DataWrapper<HTTPResponseModel>> {
        return authRepository.logoutUser(user)
    }

//    fun getChannelList(token: String):LiveData<DataWrapper<HTTPResponseModel.GetChannelList>>{
//        return chatChannelRepository.getAllChatChannel(token)
//    }
}