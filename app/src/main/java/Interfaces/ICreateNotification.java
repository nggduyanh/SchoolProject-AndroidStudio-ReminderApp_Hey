package Interfaces;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import Models.Reminder;

public interface ICreateNotification {

    public void setNotification(Reminder reminder);

    public void createChannel();

    public boolean checkPermissions (Context context);
}
