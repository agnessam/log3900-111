package com.example.colorimagemobile.classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import java.lang.NullPointerException

class ImageConvertor(val context: Context) {

    companion object ErrorMessage {
        const val Base64ToBitmap = "Error converting Base64 into Bitmap"
        const val BitmapToBase64 = "Error converting Bitmap into Base64"
    }

    // convert a base64 image to Bitmap
    fun base64ToBitmap(stringURI: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(stringURI.split(",")[1], Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (error: NullPointerException) {
            printToast(context, ErrorMessage.Base64ToBitmap)
            printMsg("${ErrorMessage.Base64ToBitmap}: $error")
            null
        }
    }

    fun bitmapToBase64() { }
}