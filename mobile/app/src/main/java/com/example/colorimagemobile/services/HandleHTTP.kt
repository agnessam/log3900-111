package com.example.colorimagemobile.services

import android.content.Context
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.HTTPResponseModel
import com.example.colorimagemobile.utils.CommonFun

class HandleHTTP{
    fun Response(context : Context, HTTPResponse: DataWrapper<HTTPResponseModel.UpdateUser>) {

        // some error occurred during HTTP request
        if (HTTPResponse.isError as Boolean) {
            context?.let { CommonFun.printToast(it, HTTPResponse.message as String) }
            return
        }
        // account update successfully
        context?.let { CommonFun.printToast(it, "Update succeed") }
    }
}