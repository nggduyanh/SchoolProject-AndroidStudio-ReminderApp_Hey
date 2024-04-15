package com.example.hey;

import static com.example.hey.NotificationReceiver.channelID;
import static com.example.hey.NotificationReceiver.messageExtra;
import static com.example.hey.NotificationReceiver.notificationID;
import static com.example.hey.NotificationReceiver.titleExtra;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import Adapters.ReminderAdapter;
import Database.DbContext;
import Fragments.AddFragment;
import Fragments.BottomSheetFragment;
import Interfaces.IClickReminderInfo;
import Interfaces.ICreateNotification;
import Models.Reminder;

public class ReminderActivity extends AppCompatActivity implements IClickReminderInfo, ICreateNotification {

    private TextView reminderTitle,goBack;
    private ImageView imageOption;
    private TextView addReminder;
    private RecyclerView reminderList;
    private int id=4;
    private FragmentContainerView fragmentContainerView;

    private ConstraintLayout containLayout,optionLayout;
    private Guideline g;

    private List<Reminder> list;
    private ReminderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        initReminderActivity();
    }

    public void addReminder(){
        reminderList.clearFocus();
//        list.add(new Reminder(6,""));
        adapter.notifyDataSetChanged();
    }

    public void initReminderActivity(){

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        list=DbContext.getInstance(this).getReminderByID(b.getInt("Id"));

        addReminder=findViewById(R.id.addReminder);
        g = findViewById(R.id.guideline_reminder);
        optionLayout = findViewById(R.id.option_view);

        goBack=findViewById(R.id.arrow_back);

        imageOption=findViewById(R.id.option);


        reminderTitle = findViewById(R.id.reminder_title);

        reminderList = findViewById(R.id.reminder_list) ;

//        list= new ArrayList<>(Arrays.asList(
//                new Reminder(1,"Thức dậy"),
//                new Reminder(2,"Đánh răng"),
//                new Reminder(3,"Đi học")
//        ));
//        int id, String reminderName, boolean flag, LocalDate date, LocalTime time,boolean status
//        list = new ArrayList<>();

        reminderList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        ReminderActivity that =this;
        adapter = new ReminderAdapter(list, this,this);
        reminderList.setAdapter(adapter);


//        addReminder.setOnClickListener(v->{
//            reminderList.clearFocus();
//            list.add(new Reminder(++id,""));
//
//            adapter.notifyDataSetChanged();
//
//        });


        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_reminder, AddFragment.newInstance(AddFragment.DuyAnh_MODE,this),null).commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(checkNotificationPermissions(this)){
                createNotificationChannel();
            }
        }
        registerForContextMenu(reminderList);


        reminderTitle.setText(b.getString("Title"));
        reminderTitle.setTextColor(b.getInt("Color"));

        goBack.setOnClickListener(v->{
            Intent resultintent = new Intent(this,MainActivity.class);
            setResult(RESULT_OK,resultintent);
            finish();
        });

    }

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {

        }
    });


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        new MenuInflater(this).inflate(R.menu.context_menu_option, menu);
    }

    @Override
    public void clickReminderInfo() {
        BottomSheetFragment detailFragment = BottomSheetFragment.newInstance(BottomSheetFragment.REMINDER_ADD);
        detailFragment.show(getSupportFragmentManager(), detailFragment.getTag());
    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ScheduleExactAlarm")
    public void scheduleNotification(Reminder reminder) {
        // Create an intent for the Notification BroadcastReceiver
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);

        // Extract title and message from user input


        // Add title and message as extras to the intent
        Bundle b = new Bundle();

        b.putString(titleExtra, reminder.getReminderName());
        b.putString(messageExtra, reminder.getReminderName());
        b.putInt(notificationID,reminder.getId());
        intent.putExtras(b);
        // Create a PendingIntent for the broadcast
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                reminder.getId(),
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Get the AlarmManager service
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Get the selected time and schedule the notification

        int minute = reminder.getTime().getMinute();
        int hour = reminder.getTime().getHour();
        int day = reminder.getDate().getDayOfMonth();
        int month = reminder.getDate().getMonthValue();
        int year = reminder.getDate().getYear();

        // Create a Calendar instance and set the selected date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        Log.d("duyanh", String.valueOf(hour));
        Log.d("duyanh", String.valueOf(minute));

        long time = calendar.getTimeInMillis();
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );
        // Show an alert dialog with information
        // about the scheduled notification
    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        // Create a notification channel for devices running
        // Android Oreo (API level 26) and above
        String name = "Main Chanel";
        String desc = "All of the Reminders";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelID, name, importance);
        channel.setDescription(desc);

        // Get the NotificationManager service and create the channel
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkNotificationPermissions(Context context) {
        // Check if notification permissions are granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            boolean isEnabled = notificationManager.areNotificationsEnabled();

            if (!isEnabled) {
                // Open the app notification settings if notifications are not enabled
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                context.startActivity(intent);

                return false;
            }
        } else {
            boolean areEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();

            if (!areEnabled) {
                // Open the app notification settings if notifications are not enabled
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                context.startActivity(intent);

                return false;
            }
        }

        // Permissions are granted
        return true;
    }
}