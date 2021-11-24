package com.example.colorimagemobile.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.colorimagemobile.R
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.Constants

object NotificationService {
    var pendingNotifications : Int = 0
    private val channelId : String =Constants.EMPTY_STRING
    private val notificationId =101


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

    fun createNotificationChannel(context : Context){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

            val name = "BEATRICE TEST"
            val descriptionText = "BREF C'EST UN TEST HEIN"
            val importance : Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId,name,importance).apply{
                description=descriptionText
            }
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(context: Context){

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Example Title")
            .setContentText("Example Description")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)){
          notify(notificationId,builder.build())
        }
    }
}