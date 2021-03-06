package com.example.stocksimulator;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;


public class BonusWorker extends Worker {

    public BonusWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        //TODO: Get last signin millis and check if current date is 24 hours past that day, if so notify bonus is ready
        Data inputData = getInputData();
        Long lastSignInMili = inputData.getLong("lastSignIn", -1);
        Calendar currentDate = Calendar.getInstance();
        boolean sendNotification = false;

        if (lastSignInMili != -1) {
            // Adding 24 hours in milliseconds to last sign in
            lastSignInMili = lastSignInMili + (3600000 * 24);
            if (currentDate.getTimeInMillis() > lastSignInMili) sendNotification = true;
        }

        if (sendNotification) {
            String notiBody = "You have money waiting for you!";

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "bonus_notification")
                    .setSmallIcon(R.drawable.ic_baseline_attach_money_24)
                    .setContentText("Daily Bonus Alert!")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notiBody))
                    .setAutoCancel(true);

            Intent actionIntent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent actionPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(actionPendingIntent);

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
        }

        return Result.success();
    }
}
