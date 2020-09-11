package com.criddam.medicine.others;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.criddam.medicine.R;
import com.criddam.medicine.database.OfflineInformation;
import com.criddam.medicine.login.views.LoginActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CridDamJobService extends JobService implements MedicineTime {
    private static final int MY_NOTIFICATION_ID = 1;
    private NotificationManager notifManager;
    private NotificationCompat.Builder builder;
    public static String CHANNEL_ID = "my_service";
    private String CHANNEL_NAME = "My_Background_Service";
    private AlarmReceiver receiver;
    public static MediaPlayer player;
    //    private TextToSpeech tts;
    private OfflineInformation offlineInformation;
    //    private Handler handler = new Handler();
//    private Runnable runnableCode;
    private int jobId;
    private DynamicVoiceConverter dynamicVoiceConverter;

    public CridDamJobService() { }

    public CridDamJobService(int job_id) {
        this.jobId = job_id;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("JobServiceUjj", "onStartJob: Job started ujjwal");
        if (!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);
        offlineInformation = new OfflineInformation(getApplicationContext());
        dynamicVoiceConverter = new DynamicVoiceConverter();
        dynamicVoiceConverter.init(offlineInformation.getUserFirstName() + offlineInformation.getUserLastName() + "   Apnar oushod khauyar somoy hoyece",
                this
        );
        createNotification(getString(R.string.it_s_time_to_take_your_medicine), MY_NOTIFICATION_ID);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        EventBus.getDefault().unregister(this);
        stopSelf();
        return true;
    }

    public void createNotification(String aMessage, int notificationId) {
        String description = "notification";
        Intent intent;
        PendingIntent pendingIntent;

        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        EventBus.getDefault().post(getApplicationContext().getString(R.string.it_s_time_to_take_your_medicine));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel;
            if (notifManager != null) {
                mChannel = notifManager.getNotificationChannel(CHANNEL_ID);
                mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
                mChannel.setDescription(description);
                mChannel.setSound(null, null);
//                mChannel.enableVibration(true);
//                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra("notification_type", "medicine");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder
                    //.setContentTitle(aMessage) // required
                    .setSmallIcon(getNotificationIcon())
                    .setContentText(this.getString(R.string.app_name))  // required
//                    .setDefaults(NotificationRoomModel.DEFAULT_ALL)
                    .setAutoCancel(true)
//                    .setDefaults(0)
                    .setWhen(0)
                    .setSound( Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.repeated_sound))
//                    .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                    .setContentIntent(pendingIntent);
                   // .setTicker(aMessage);
//                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        } else {
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            intent = new Intent(this, LoginActivity.class);
            intent.putExtra("notification_type", "medicine");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder
                    //.setContentTitle(aMessage)
                    .setSmallIcon(getNotificationIcon())
                    .setContentText(this.getString(R.string.app_name))  // required
//                    .setDefaults(NotificationRoomModel.DEFAULT_ALL)
                    .setAutoCancel(true)
//                    .setDefaults(0)
                    .setWhen(0)
                    .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.repeated_sound))
//                    .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                    .setContentIntent(pendingIntent);
                   // .setTicker(aMessage);
//                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        }

        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_reminder);
//        remoteViews.setOnClickPendingIntent(R.id.tv, "New Name");
//        listener(remoteViews,getApplicationContext());


        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        builder.setWhen(0);
        builder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.repeated_sound));
        builder.setCustomContentView(remoteViews);
        notifManager.notify(notificationId, builder.build());
//        stopSelf();
        JobScheduler jobScheduler = (JobScheduler)
                getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null)
            jobScheduler.cancel(jobId);
//        if (dynamicVoiceConverter != null) {
//            dynamicVoiceConverter.shutDown();
//        }
    }

    private void listener(RemoteViews remoteViews, Context applicationContext) {
        // you have to make intetns for each action (your Buttons)
        Intent intent = new Intent("Okay");
        Intent intent2 = new Intent("Cancel");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(applicationContext, 1, intent, 0);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(applicationContext, 1, intent2, 0);

        // add actions here !
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Okay");
        intentFilter.addAction("Cancel");
//        player = MediaPlayer.create(applicationContext, R.raw.repeated_sound);
//        player.start();
        AlarmReceiver receiver = new AlarmReceiver();
        applicationContext.registerReceiver(receiver, intentFilter);
        remoteViews.setOnClickPendingIntent(R.id.btn_take_medicine_ok, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.btn_will_take_medicine_later, pendingIntent2);
    }


    private int getNotificationIcon() {
        return R.mipmap.ic_launcher;
    }

    @Override
    public void onDestroy() {
        //Close the Text to Speech Library
        if (dynamicVoiceConverter != null) {
            dynamicVoiceConverter.shutDown();
        }
        super.onDestroy();
    }

    @Subscribe
    @Override
    public void getNotified( String timeToTakeMedicine) {
    }

}