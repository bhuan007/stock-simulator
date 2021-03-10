package com.example.stocksimulator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.Calendar;

public class AlertReceiver extends BroadcastReceiver {
    private static final String TAG = "AlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: received alert intent");

        String tag = "bonus_notification";

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(BonusWorker.class)
                .setConstraints(constraints)
                .addTag(tag)
                .build();

        WorkManager.getInstance(context).enqueue(workRequest);

    }

}
