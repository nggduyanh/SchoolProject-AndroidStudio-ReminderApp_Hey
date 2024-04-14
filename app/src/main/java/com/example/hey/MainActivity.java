package com.example.hey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Adapters.GroupAdapter;
import Adapters.ListReminderAdapter;
import Database.DatabaseReminder;
import Database.DbContext;
import Fragments.AddFragment;
import Interfaces.IUpdateDatabase;
import ItemDecoration.ItemDivider;
import ItemDecoration.MarginGroupItem;
import Models.Group;
import Models.ListReminder;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MainActivity extends AppCompatActivity implements IUpdateDatabase {

    private RecyclerView groupReminderRV;
    private GroupAdapter adapterGroup;
    private RecyclerView listReminderRV;
    private ImageButton deleteEditTextText;
    private EditText editTextMain;
    private ConstraintLayout layout ;
    private Guideline g;
    private List <Group> groupList;
    private List<ListReminder> listReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMainActivityComponents();
        setMainLayoutPadding();

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
        initGroupReminderComponents();
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
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragment_container_main, AddFragment.class,null).commit();
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
        ListReminderAdapter adapter = new ListReminderAdapter(listReminders);
        ItemDivider decoration  = new ItemDivider(this, DividerItemDecoration.VERTICAL);
        listReminderRV.setAdapter(adapter);
        listReminderRV.addItemDecoration(decoration);
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
        listReminderRV.setAdapter(new ListReminderAdapter(listReminders));
        Toast.makeText(this, DbContext.getInstance(this).getLastRowReminder().getReminderName() + "", Toast.LENGTH_SHORT).show();
    }
}