package com.example.colorimagemobile.httpresponsehandler

import android.content.Context
import com.example.colorimagemobile.models.DataWrapper
import com.example.colorimagemobile.models.TextChannelModel
import com.example.colorimagemobile.services.chat.TextChannelService
import com.example.colorimagemobile.utils.CommonFun


class TextChannelHandler {

    fun responseGetAll(context : Context, HTTPResponse: DataWrapper<List<TextChannelModel.AllInfo>>) {

        // some error occurred during HTTP request
        if (HTTPResponse.isError as Boolean) {
            context?.let { CommonFun.printToast(it, HTTPResponse.message as String) }
            return
        }

        // set all channel when request successful
        TextChannelService.setAllTextChannel(HTTPResponse.data!!)

        // request successfully
        context?.let { CommonFun.printToast(it, "All channel get success") }
    }
}