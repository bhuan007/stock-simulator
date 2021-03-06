package com.example.stocksimulator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Firebase firebase = new Firebase();
        firebase.getLastSignInDate(new Firebase.OnGetLastSignInDate() {
            @Override
            public void onGetLastSignInDate(Calendar lastSignInReturned) {
                Calendar lastSignIn = lastSignInReturned;

                Data data = null;

                data = new Data.Builder()
                        .putLong("lastSignIn", lastSignIn.getTimeInMillis())
                        .build();

                String tag = "bonus_notification";

                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(BonusWorker.class)
                        .setInputData(data)
                        .setConstraints(constraints)
                        .addTag(tag)
                        .build();

                WorkManager.getInstance(context).enqueue(workRequest);

            }
        });


    }
}