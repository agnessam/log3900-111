package com.example.colorimagemobile.classes.Notification

import android.content.Context
import android.media.RingtoneManager
import android.service.notification.NotificationListenerService
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import com.example.colorimagemobile.ui.home.HomeActivity


class Notification {
    private var pendingNotifications : Int = 0

    fun playSound(context: Context){
//        val context : Context = getSystemService("notification")!!
        try{
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(context, defaultSoundUri)
            r.play()
        } catch (e: Exception){e.printStackTrace()}
    }

    fun setCounter(){
        this.pendingNotifications++
    }

    fun getCounter(): Int{
       return this.pendingNotifications
    }


}