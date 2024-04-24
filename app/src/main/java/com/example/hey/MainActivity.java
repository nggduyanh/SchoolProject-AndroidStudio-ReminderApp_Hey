package com.example.hey;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import Adapters.GroupAdapter;
import Adapters.ListReminderAdapter;

import Adapters.ReminderAdapter;

import Database.DatabaseReminder;
import Database.DbContext;

import Fragments.AddFragment;

import Interfaces.ICallReminderActivity;

import Fragments.BottomSheetFragment;

import Interfaces.ICreateNotification;
import Interfaces.IUpdateDatabase;
import ItemDecoration.ItemDivider;
import ItemDecoration.MarginGroupItem;
import Models.Group;
import Models.ListReminder;
import Models.Reminder;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MainActivity extends AppCompatActivity implements IUpdateDatabase, ICallReminderActivity, ICreateNotification {

    private RecyclerView groupReminderRV;
    private GroupAdapter adapterGroup;
    private RecyclerView listReminderRV;
    private ImageButton deleteEditTextText;
    private EditText editTextMain;
    private ConstraintLayout layout ;
    private Guideline g;

    private int index;
    private List <Group> groupList;
    private List<ListReminder> listReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMainActivityComponents();
        setMainLayoutPadding();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(checkPermissions(this)){
                createChannel();
            }
        }
    }


    private void setMainLayoutPadding()
    {
        layout = findViewById(R.id.main_layout);
        g = findViewById(R.id.guideLine_main);
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int[] p = new int[2];
                int[] p2 = new int[2];
                Log.d("helloPa", "" + layout.getHeight() + " " + layout.getWidth());
                Log.d("helloPa", "" + layout.getMeasuredHeight() + " " + layout.getMeasuredWidth());
                g.getLocationInWindow(p);
                getSupportFragmentManager().findFragmentById(R.id.fragment_container_main).getView().getLocationOnScreen(p2);
//                Log.d("phananh2","Location: "  + p[0] + " " + p[1]);
                int resultPadding = p[1] / 90 * 10;
                layout.setPadding(0,0,0,resultPadding);
//                Log.d("phananh","Location: " + p2[0] + " " + p2[1]);
            }
        });
    }
    private void initMainActivityComponents() {
        initLists();
//        initGroupReminderComponents();
        initListReminderComponents();
        initSearchComponents();
        initFragment();
        setupBlur();

    }

    private void initLists() {

         groupList = new ArrayList<>(Arrays.asList(
                new Group(1,"Lịch dự kiến",1),
                new Group(2,"Tất cả",1),
//                new Group(3, "Hôm nay",1),
                new Group(4,"Ngày mai",5)
        ));

        listReminders = DbContext.getInstance(this).getListReminder();
    }

    private void setupBlur() {
        ViewGroup rootView = findViewById(R.id.root);
        Drawable windowBackground = getWindow().getDecorView().getBackground();
        BlurView blur = findViewById(R.id.blur_fragment);
        blur.setupWith(rootView,new RenderScriptBlur(this))
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(15f)
                .setBlurAutoUpdate(true);
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragment_container_main, AddFragment.newInstance(AddFragment.Main_MODE,this),null).commit();
    }

    private void initGroupReminderComponents()
    {
        groupReminderRV = findViewById(R.id.group_reminder_recyleview);

        GridLayoutManager groupReminderLayout = new GridLayoutManager(this,2);
        groupReminderLayout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == adapterGroup.getItemCount() - 1 && position % 2 == 0)
                {
                    return 2;
                }
                else return 1;
            }
        });
        MarginGroupItem itemDecoration = new MarginGroupItem(30,30);
        groupReminderRV.addItemDecoration(itemDecoration);
        groupReminderRV.setLayoutManager(groupReminderLayout);
        adapterGroup = new GroupAdapter(groupList);
        groupReminderRV.setAdapter(adapterGroup);
    }

    private void initListReminderComponents ()
    {
        listReminderRV = findViewById(R.id.list_reminder_recycleview);
        listReminderRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ListReminderAdapter adapter = new ListReminderAdapter(listReminders,this);

        ItemDivider decoration  = new ItemDivider(this, DividerItemDecoration.VERTICAL);
        listReminderRV.setAdapter(adapter);
        listReminderRV.addItemDecoration(decoration);
        registerForContextMenu(listReminderRV);
    }

    private void initSearchComponents ()
    {
        deleteEditTextText = findViewById(R.id.delete_editText_main);
        editTextMain = findViewById(R.id.search_bar);
        editTextMain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextMain.getText().length() == 0)
                {
                    deleteEditTextText.setVisibility(View.GONE);
                }
                else
                {
                    deleteEditTextText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        deleteEditTextText.setOnClickListener(v -> {
            editTextMain.getText().clear();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("paResume","continued");
    }

    @Override
    public void updateInterface()
    {
        listReminders = DbContext.getInstance(this).getListReminder();
        listReminderRV.setAdapter(new ListReminderAdapter(listReminders,this));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        ListReminderAdapter a = (ListReminderAdapter)listReminderRV.getAdapter();
        int pos = a.getLongClickPosition();
        ListReminder obj = listReminders.get(pos);
        if (item.getItemId() == R.id.update)
        {
            BottomSheetFragment groupFragment = BottomSheetFragment.newInstance(BottomSheetFragment.LIST_UPDATE,obj);
            groupFragment.show(getSupportFragmentManager(), groupFragment.getTag());
        }
        else if (item.getItemId() == R.id.delete)
        {
            DbContext.getInstance(this).delete(obj);
            listReminders.remove(pos);
            listReminderRV.getAdapter().notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);

    }

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            updateInterface();
        }
    });

    @Override
    public void getListPosition(int position) {
        index=position;
    }
    @Override
    public void intentCall() {
        Intent intent = new Intent(this,ReminderActivity.class);
        ListReminder listReminder = listReminders.get(index);
        Bundle b = new Bundle();
        b.putInt("Id",listReminder.getId());
        b.putString("Title",listReminder.getListName());
        b.putInt("Color",listReminder.getColor());
        int id =listReminder.getId();
        intent.putExtras(b);
        launcher.launch(intent);
    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setNotification(Reminder reminder) {

        Intent intent = new Intent(this, NotificationReceiver.class);


        Bundle b = new Bundle();

        b.putString("messageExtra", reminder.getReminderName());
        b.putInt("notificationID",reminder.getId());
        intent.putExtras(b);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                reminder.getId(),
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        int minute = reminder.getTime().getMinute();
        int hour = reminder.getTime().getHour();
        int day = reminder.getDate().getDayOfMonth();
        int month = reminder.getDate().getMonthValue()-1;
        int year = reminder.getDate().getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);


        long time = calendar.getTimeInMillis();
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
        );
    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannel() {

        String name = "Main Chanel";
        String desc = "All of the Reminders";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("MainChannel", name, importance);
        channel.setDescription(desc);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkPermissions(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            boolean isEnabled = notificationManager.areNotificationsEnabled();

            if (!isEnabled) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                context.startActivity(intent);

                return false;
            }
        } else {
            boolean areEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();

            if (!areEnabled) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                context.startActivity(intent);

                return false;
            }
        }

        return true;
    }
}