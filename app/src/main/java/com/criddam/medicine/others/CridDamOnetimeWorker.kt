package com.criddam.medicine.others

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.criddam.medicine.R
import com.criddam.medicine.database.OfflineInformation
import com.criddam.medicine.login.views.LoginActivity
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

class CridDamOnetimeWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private var offlineInformation: OfflineInformation? = null

    override fun doWork(): Result {
        offlineInformation = OfflineInformation(applicationContext)
        // Firebase
        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference
        val rand = (0..100).random()
        myRef.child("alarm").setValue(rand)
        offlineInformation!!.saveUserAlarm(true)
        createNotification(
            context,
            context.getString(R.string.it_s_time_to_take_your_medicine), 1
        )
        return Result.success()
    }


    fun createNotification(context: Context, aMessage: String?, notificationId: Int) {
        val dynamicVoiceConverter = DynamicVoiceConverter()
        dynamicVoiceConverter.init(
            offlineInformation!!.userFirstName + offlineInformation!!.userLastName + "  Apnar oushod khauyar somoy hoyece",
            applicationContext
        )
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
            var mChannel: NotificationChannel?
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
            builder //.setContentTitle(aMessage)
                .setSmallIcon(getNotificationIcon())
                .setContentText(context.getString(R.string.app_name))
                .setAutoCancel(true)
                .setWhen(0)
                .setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.repeated_sound))
                .setContentIntent(pendingIntent)
            //.setTicker(aMessage)
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