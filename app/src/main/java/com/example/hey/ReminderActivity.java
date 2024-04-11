package com.example.hey;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Adapters.ReminderAdapter;
import Fragments.AddFragment;
import Models.Reminder;

public class ReminderActivity extends AppCompatActivity {

    private TextView reminderTitle,goBack;
    private ImageView imageOption;

    private RecyclerView reminderList;

    private FragmentContainerView fragmentContainerView;

    private ConstraintLayout containLayout,optionLayout;
    private Guideline g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        initReminderActivity();
    }

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {

        }
    });

    public void initReminderActivity(){

//        containLayout = findViewById(R.id.reminder_layout);
//        g = findViewById(R.id.guideline_reminder);
//        containLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                containLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                int[] p = new int[2];
//                int[] p2 = new int[2];
//                g.getLocationInWindow(p);
//                getSupportFragmentManager().findFragmentById(R.id.fragment_container_main).getView().getLocationOnScreen(p2);
//            }
//        });

        optionLayout = findViewById(R.id.option_view);

        goBack=findViewById(R.id.arrow_back);

        imageOption=findViewById(R.id.option);


        reminderTitle = findViewById(R.id.reminder_title);

        reminderList = findViewById(R.id.reminder_list) ;

        List<Reminder> list= new ArrayList<>(Arrays.asList(
                new Reminder(1,"Thức dậy"),
                new Reminder(2,"Đánh răng"),
                new Reminder(3,"Đi học")
        ));

        reminderList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ReminderAdapter adapter = new ReminderAdapter(list);
        reminderList.setAdapter(adapter);

        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragment_container_reminder, AddFragment.class,null).commit();

    }

}