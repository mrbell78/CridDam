package com.criddam.medicine.others

import android.app.IntentService
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.criddam.medicine.R
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.login.views.LoginActivity

class AlarmService : IntentService("AlarmService") {

    public override fun onHandleIntent(intent: Intent?) {
        Log.e("AlarmServiceUjjwal" , "AlarmUjjwal")
        val dynamicVoiceConverter = DynamicVoiceConverter()
        val offlineInformation = OfflineInformation(applicationContext)
        dynamicVoiceConverter.init(offlineInformation.userFirstName+offlineInformation.userLastName+ "   Apnar oushod khauyar somoy hoyece",
            this
        )
        sendNotification()
    }

    private fun sendNotification() {
        val pendingIntent: PendingIntent
        val CHANNEL_ID = "my_service"
        val notificationLayout =
            RemoteViews(packageName, R.layout.custom_reminder)
        val notificationLayoutExpanded =
            RemoteViews(packageName, R.layout.custom_reminder)
//        if (Build.VERSION.SDK_INT >= 26) {
        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val offlineInformation = OfflineInformation(this)
        val dynamicVoiceConverter = DynamicVoiceConverter()
        dynamicVoiceConverter.init(
            offlineInformation.userFirstName +
                    " " + offlineInformation.userLastName + "   Apnar oushod khauyar somoy hoyece",
            this
        )

        var intent = Intent(applicationContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("notification_type", "medicine")
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val notification = mBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
            .setAutoCancel(true)
//                    .setDefaults(0)
            .setWhen(0)
            .setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.repeated_sound))
//                    .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setCustomContentView(notificationLayout)
            .build()

        mNotificationManager.notify(1, mBuilder.build())
        startForeground(100, notification)
//        } else
//            startForeground(100, Notification())
        Log.e("AlarmService", "Notification sent.")
        stopSelf()
    }
}