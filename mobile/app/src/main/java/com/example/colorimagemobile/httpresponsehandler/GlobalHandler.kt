package com.example.colorimagemobile.httpresponsehandler

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.models.UserModel
import com.example.colorimagemobile.repositories.UserRepository
import com.example.colorimagemobile.services.UserService
import com.example.colorimagemobile.ui.home.HomeActivity
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.imageView
import com.example.colorimagemobile.utils.CommonFun.Companion.loadUrl
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.CommonFun.Companion.usernameMenuItem

class GlobalHandler{

    // for all update and delete request
    fun response(context : Context, HTTPResponse: DataWrapper<HTTPResponseModel.UserResponse>) {

        // some error occurred during HTTP request
        if (HTTPResponse.isError as Boolean) {
            context?.let { CommonFun.printToast(it, HTTPResponse.message as String) }
            return
        }
        // request successfully
        context?.let { CommonFun.printToast(it, "Request succeed") }
        printMsg("new user all info in handler before refresh  "+HTTPResponse.data)

        refreshUserData(UserService.getToken()).observe(HomeActivity(), { handleGetUserMe(it) })
    }

    fun refreshUserData (token: String): LiveData<DataWrapper<HTTPResponseModel.UserResponse>> {
        return UserRepository().getUserByToken(token)
    }


    private fun handleGetUserMe(response: DataWrapper<HTTPResponseModel.UserResponse>) {
        UserService.setUserInfo(response.data?.user as UserModel.AllInfo)
        printMsg("new user all info in handler  "+response.data)

        // update username in menu item
        usernameMenuItem.text = UserService.getUserInfo().username

        //update user avatar
        loadUrl(UserService.getUserInfo().avatar.imageUrl,imageView)

    }
}