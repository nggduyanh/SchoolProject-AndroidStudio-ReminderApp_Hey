package com.example.hey;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.hey.R;

import java.time.LocalDate;


public class NotificationReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();

        Notification notification = new NotificationCompat.Builder(context, "channelID")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Hey")
                .setContentText(b.getString("messageExtra"))
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(b.getInt("notificationID"), notification);

        Log.d("receiver","id:"+b.getInt("notificationID"));
    }


}
