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

// Constants for notification

// BroadcastReceiver for handling notifications

public class NotificationReceiver extends BroadcastReceiver {

    public static final String notificationID = "notificationID";
    public static final String channelID = "MainChannel";
    public static final String titleExtra = "titleExtra";
    public static final String messageExtra = "messageExtra";

    @RequiresApi(api = Build.VERSION_CODES.O)
    // Method called when the broadcast is received
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        // Build the notification using NotificationCompat.Builder
        Notification notification = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(b.getString(titleExtra)) // Set title from intent
                .setContentText(b.getString(messageExtra)) // Set content text from intent
                .build();

        // Get the NotificationManager service
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Show the notification using the manager
        manager.notify(b.getInt(notificationID), notification);

        Log.d("receiver","id:"+b.getInt(notificationID));
    }


}
