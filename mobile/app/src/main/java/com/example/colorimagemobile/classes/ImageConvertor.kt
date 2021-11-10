package com.example.colorimagemobile.classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Base64
import com.caverock.androidsvg.SVG
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.CommonFun.Companion.printToast
import java.io.ByteArrayOutputStream
import java.lang.NullPointerException
import java.nio.charset.StandardCharsets
import kotlin.math.ceil

class ImageConvertor(val context: Context) {

    companion object ErrorMessage {
        const val Base64ToBitmap = "Error converting Base64 into Bitmap"
        const val BitmapToBase64 = "Error converting Bitmap into Base64"
    }

    // convert a base64 image to Bitmap: https://stackoverflow.com/questions/37327038/svg-data-uri-to-bitmap
    fun base64ToBitmap(dataURI: String): Bitmap? {
        return try {
            // remove data:image/svg+xml;base64, from URI and decode String
            val base64Data = dataURI.replace("data:image/svg+xml;base64,", "");
            val imageBytes = Base64.decode(base64Data, Base64.DEFAULT);
            val svgAsString = String(imageBytes, StandardCharsets.UTF_8)
            val svg: SVG = SVG.getFromString(svgAsString)

            // Create a bitmap and canvas to draw onto
            val svgWidth = svg.documentWidth
            val svgHeight = svg.documentHeight

            val newBitmap = Bitmap.createBitmap(
                ceil(svgWidth.toDouble()).toInt(),
                ceil(svgHeight.toDouble()).toInt(),
                Bitmap.Config.ARGB_8888
            )
            val bitmapCanvas = Canvas(newBitmap)

            // TO CHANGE
            bitmapCanvas.drawRGB(255, 255, 255)

            // Render our document onto our canvas
            svg.renderToCanvas(bitmapCanvas);

            return newBitmap
        } catch (error: NullPointerException) {
            printToast(context, ErrorMessage.Base64ToBitmap)
            printMsg("${ErrorMessage.Base64ToBitmap}: $error")
            null
        }
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val bytesArray = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytesArray)
        val bytes = bytesArray.toByteArray()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}