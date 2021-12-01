package com.example.colorimagemobile.classes.NotificationSound

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import com.example.colorimagemobile.utils.Constants

class Notification {

    fun playSound(context: Context){
        try{
            val customSoundUri: Uri = Uri.parse(Constants.NOTIFICATION_URI)
            val r = RingtoneManager.getRingtone(context, customSoundUri)
            r.play()
        } catch (e: Exception){e.printStackTrace()}
    }

}