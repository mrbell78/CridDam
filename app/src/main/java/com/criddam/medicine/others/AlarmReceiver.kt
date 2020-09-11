package com.criddam.medicine.others

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.criddam.medicine.R
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.login.views.LoginActivity
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class AlarmReceiver : BroadcastReceiver() {
    private lateinit var mContext: Context
    private lateinit var offlineInformation:OfflineInformation


    override fun onReceive(context: Context, intent: Intent) {
        mContext = context
        offlineInformation = OfflineInformation(context)



        val setHour = intent.getIntExtra("HOUR_OF_DAY", -1)
        val setMinute = intent.getIntExtra("MINUTE_OF_HOUR", -1)
        val rightNow = Calendar.getInstance()
        val currentHour: Int = rightNow.get(Calendar.HOUR_OF_DAY)
        val currentMinute: Int = rightNow.get(Calendar.MINUTE)


        if (intent.action.equals("Okay", ignoreCase = true)) {
            Toast.makeText(context, context.getString(R.string.ok), Toast.LENGTH_SHORT).show()
            //Toast.makeText(context,"ok baby",Toast.LENGTH_SHORT);
        } else if (intent.action.equals("Cancel", ignoreCase = true)) {
            Toast.makeText(context, context.getString(R.string.take_later), Toast.LENGTH_SHORT)
                .show()
            //Toast.makeText(context,"no baby",Toast.LENGTH_SHORT);
        } else if (intent.action.equals("play", ignoreCase = true)) {
            if (currentHour == setHour) {
                if (currentMinute == setMinute) {
                    Log.e("AlaRecvEntered", "Ujj")
                    Toast.makeText(
                        context,
                        R.string.it_s_time_to_take_your_medicine,
                        Toast.LENGTH_SHORT
                    ).show()
                    // Firebase
                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.reference
                    val rand = (0..100).random()
                    myRef.child("alarm").setValue(rand)
                    offlineInformation.saveUserAlarm(true)
                    createNotification(
                        context,
                        context.getString(R.string.it_s_time_to_take_your_medicine), 1
                    )
                }
            }
        }
    }


    fun createNotification(context: Context, aMessage: String?, notificationId: Int) {
        var notifManager: NotificationManager? = null
        var builder: NotificationCompat.Builder
        val CHANNEL_NAME = "My_Background_Service"
        val description = "notification"
        val intent: Intent
        val pendingIntent: PendingIntent
        if (notifManager == null) {
            notifManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            var mChannel: NotificationChannel? = null
            mChannel =
                NotificationChannel(CridDamJobService.CHANNEL_ID, CHANNEL_NAME, importance)
            mChannel.description = description
            mChannel.setSound(null, null)
            notifManager.createNotificationChannel(mChannel)
            builder =
                NotificationCompat.Builder(context, CridDamJobService.CHANNEL_ID)
            intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("notification_type", "medicine")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            builder.setSmallIcon(getNotificationIcon())
                .setContentText(context.getString(R.string.app_name))
                .setAutoCancel(true) //                    .setDefaults(0)
                .setWhen(0)
                .setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.repeated_sound))
                .setContentIntent(pendingIntent)
            //.setTicker(aMessage)
        } else {
            builder =
                NotificationCompat.Builder(context, CridDamJobService.CHANNEL_ID)
            intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("notification_type", "medicine")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            builder
                .setSmallIcon(getNotificationIcon())
                .setContentText(context.getString(R.string.app_name))
                .setAutoCancel(true)
                .setWhen(0)
                .setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.repeated_sound))
                .setContentIntent(pendingIntent)
            //  .setTicker(aMessage)
        }
        val remoteViews =
            RemoteViews(context.packageName, R.layout.custom_reminder)
        builder.priority = NotificationCompat.PRIORITY_DEFAULT
        val stackBuilder =
            TaskStackBuilder.create(context)
        stackBuilder.addParentStack(LoginActivity::class.java)
        stackBuilder.addNextIntent(intent)
        val resultPendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(resultPendingIntent)
        builder.setWhen(0)
        builder.setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.repeated_sound))
        builder.setCustomContentView(remoteViews)
        notifManager.notify(notificationId, builder.build())
    }

    private fun getNotificationIcon(): Int {
        return R.mipmap.ic_launcher
    }
}