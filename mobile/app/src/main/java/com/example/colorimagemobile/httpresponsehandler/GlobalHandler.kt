package com.example.colorimagemobile.httpresponsehandler

import android.content.Context
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.services.users.UserService
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg

class GlobalHandler{

    // for all update and delete request for user
    fun response(context : Context, HTTPResponse: DataWrapper<HTTPResponseModel.UserResponse>) {
        printMsg("in handler check data use for update  = "+ UserService.getNewProfileData())
        // some error occurred during HTTP request
        if (HTTPResponse.isError as Boolean) {
            context?.let { CommonFun.printToast(it, HTTPResponse.message as String) }
            printMsg("response is actually error = "+ HTTPResponse.isError)
            return
        }
        printMsg("response is not error = "+ HTTPResponse.data)
        printMsg("check response is error value = "+ HTTPResponse.isError)

        // request successfully
        context?.let { CommonFun.printToast(it, "Request succeed") }
    }
}