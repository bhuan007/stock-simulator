package com.example.stocksimulator;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;



public class BonusWorker extends Worker {
    private static final String TAG = "BonusWorker";

    public BonusWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("bonus_notification", "bonus_notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Log.d(TAG, "doWork: About to send general notification");
        String notiBody = "Come invest in some stocks!";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "bonus_notification")
                .setSmallIcon(R.drawable.ic_baseline_attach_money_24)
                .setContentText("The market never sleeps.")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notiBody))
                .setAutoCancel(true);

        Intent actionIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(actionPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        AlarmHelper.cancelAlarm(getApplicationContext(), 0);

        return Result.success();
    }
}
