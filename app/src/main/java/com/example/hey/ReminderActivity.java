package com.example.hey;

import static com.example.hey.NotificationReceiver.channelID;
import static com.example.hey.NotificationReceiver.messageExtra;
import static com.example.hey.NotificationReceiver.notificationID;
import static com.example.hey.NotificationReceiver.titleExtra;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import Adapters.ReminderAdapter;
import Adapters.ReminderViewHolder;
import Database.DbContext;
import Fragments.AddFragment;
import Fragments.BottomSheetFragment;
import Interfaces.IClickReminderInfo;
import Interfaces.ICreateNotification;
import Interfaces.IUpdateDatabase;
import Models.ListReminder;
import Models.Reminder;

public class ReminderActivity extends AppCompatActivity implements IClickReminderInfo, ICreateNotification, IUpdateDatabase {

    private int listID;
    private TextView reminderTitle,goBack;
    private ImageView imageOption;
    private TextView addReminder;
    private RecyclerView reminderList;
    private FragmentContainerView fragmentContainerView;

    private ConstraintLayout containLayout,optionLayout;
    private Guideline g;

    private List<Reminder> list;
    private ReminderAdapter adapter;
    private int index = -1;

    private TextView doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        initReminderActivity();
    }

    public void addReminder(){
        Log.d("paDebug","hihi");
        doneBtn.setVisibility(View.VISIBLE);
        adapter.getList().add(new Reminder());
        index = list.size() - 1;
        adapter.notifyDataSetChanged();
    }

    public void initReminderActivity()
    {
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        listID=b.getInt("Id");
        list=DbContext.getInstance(this).getReminderByListID(listID);

        doneBtn = findViewById(R.id.doneBtn);
        doneBtn.setVisibility(View.GONE);
        doneBtn.setOnClickListener(v -> {
            if (index != -1)
            {
               EditText txt = ((ReminderViewHolder)reminderList.findViewHolderForAdapterPosition(index)).reminderName;
               if (txt.getText().length() == 0)
               {
                   list.remove(index);
                   adapter.notifyDataSetChanged();
                   reminderList.clearFocus();
               }
               else
               {
                   list.get(index).setReminderName(txt.getText().toString());
                   list.get(index).setListReminder(new ListReminder(listID));
                   DbContext.getInstance(this).add(list.get(index));
                   list.get(index).setId(DbContext.getInstance(this).getLastRowReminder().getId());
               }
                index = -1;
               doneBtn.setVisibility(View.GONE);
            }
        });
        addReminder=findViewById(R.id.addReminder);
        g = findViewById(R.id.guideline_reminder);
        optionLayout = findViewById(R.id.option_view);

        goBack=findViewById(R.id.arrow_back);
//        Drawable back = this.getCon(R.drawable.back_icon);
//        back.setTint(Color.RED);
//        goBack.setCompoundDrawablesRelative(back,null,null,null);

//        imageOption=findViewById(R.id.option);


        reminderTitle = findViewById(R.id.reminder_title);

        reminderList = findViewById(R.id.reminder_list) ;



        reminderList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        ReminderActivity that =this;
        adapter = new ReminderAdapter(list, this,this,this);
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

        ConstraintLayout root = findViewById(R.id.reminder_layout);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (index != -1)
                {
                    list.remove(index);
                    adapter.notifyDataSetChanged();
                    index = -1;
                    reminderList.clearFocus();
                    doneBtn.setVisibility(View.GONE);
                }
            }
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
//        new MenuInflater(this).inflate(R.menu.context_menu_option, menu);
    }

    @Override
    public void clickReminderInfo(int position) {
        Reminder reminder= list.get(position);
        BottomSheetFragment detailFragment = BottomSheetFragment.newInstance(BottomSheetFragment.REMINDER_UPDATE,reminder);
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
        int month = reminder.getDate().getMonthValue()-1;
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

    @Override
    public void updateInterface() {
        list = DbContext.getInstance(this).getReminderByListID(listID);
        reminderList.setAdapter(new ReminderAdapter(list,this,this,this));
    }
}