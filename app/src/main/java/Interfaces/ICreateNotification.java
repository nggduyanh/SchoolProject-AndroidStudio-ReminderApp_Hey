package Interfaces;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import Models.Reminder;

public interface ICreateNotification {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ScheduleExactAlarm")
    public void scheduleNotification(Reminder reminder);

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel ();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkNotificationPermissions (Context context);
}
