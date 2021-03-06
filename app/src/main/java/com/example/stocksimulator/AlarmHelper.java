package com.example.stocksimulator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmHelper {
    private static final String TAG = "AlarmHelper";



    public static void initAlarm(Context context, int id, long triggermillis, long intervalMili) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggermillis, intervalMili, pendingIntent);
        Log.d(TAG, "initAlarm: Alarm set for " + triggermillis);
    }

    public static void cancelAlarm(Context context, int id ) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);

        alarmManager.cancel(pendingIntent);
        Log.d(TAG, "Alarm cancelled for id " + id);
    }
}
