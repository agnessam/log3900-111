package com.example.colorimagemobile.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.colorimagemobile.R
import com.example.colorimagemobile.services.socket.ChatSocketService
import com.example.colorimagemobile.utils.CommonFun
import com.example.colorimagemobile.utils.CommonFun.Companion.printMsg
import com.example.colorimagemobile.utils.Constants

object NotificationService {
    var pendingNotifications : Int = 0
//    private val channelId : String =Constants.EMPTY_STRING
//    private val notificationId =101
    private  var listOfPendingMessage : HashMap<String, Int> = HashMap()
    private  var tmpPendingMessage : HashMap<String, Int> = HashMap()
    private var realCount : Int = 0


    fun playSound(context: Context){
        try{
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(context, defaultSoundUri)
            r.play()
        } catch (e: Exception){e.printStackTrace()}
    }

    fun setCounter(value:Int){
        pendingNotifications = value
    }

    fun getCounter(): Int{
        return pendingNotifications
    }

    fun addToPendingMessage(newPendingMessage: HashMap<String,Int>){
        this.tmpPendingMessage = newPendingMessage
    }

    fun getPendingNotification() :HashMap<String,Int> {
        tmpPendingMessage.keys.forEach { key ->
            realCount =tmpPendingMessage[key]!!/Constants.LISTEN_MESSAGE_OCCURENCE
            listOfPendingMessage[key]= realCount
        }
        printMsg("listofpendingmessage dans notification = "+ listOfPendingMessage)
        return this.listOfPendingMessage
    }

//    @SuppressLint("ObsoleteSdkInt")
//    fun createNotificationChannel(context : Context){
//        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
//
//            val name = "BEATRICE TEST"
//            val descriptionText = "BREF C'EST UN TEST HEIN"
//            val importance : Int = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(channelId,name,importance).apply{
//                description=descriptionText
//            }
//            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

//    fun sendNotification(context: Context){
//
//        val builder = NotificationCompat.Builder(context, channelId)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle("Example Title")
//            .setContentText("Example Description")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        with(NotificationManagerCompat.from(context)){
//          notify(notificationId,builder.build())
//        }
//    }
}