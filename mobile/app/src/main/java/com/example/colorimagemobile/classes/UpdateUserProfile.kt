package com.example.colorimagemobile.classes

import androidx.lifecycle.LiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.users.UserService

class UpdateUserProfile {

    //call retrofit request to database to update user info
    fun updateUserInfo(dataToUpdate: UserModel.UpdateUser): LiveData<DataWrapper<HTTPResponseModel.UserResponse>> {
        return UserRepository().updateUserData(dataToUpdate,UserService.getToken(), UserService.getUserInfo()._id)
    }
}