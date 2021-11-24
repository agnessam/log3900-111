package com.example.colorimagemobile.services

import android.content.Context
import android.media.RingtoneManager
import com.example.colorimagemobile.utils.CommonFun

object NotificationService {
    var pendingNotifications : Int = 0

    fun playSound(context: Context){
        try{
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(context, defaultSoundUri)
            r.play()
        } catch (e: Exception){e.printStackTrace()}
    }

    fun setCounter(value:Int){
        pendingNotifications = value
        CommonFun.printMsg("count = " + pendingNotifications)
    }

    fun getCounter(): Int{
        CommonFun.printMsg("get count = " + pendingNotifications)
        return pendingNotifications
    }
}