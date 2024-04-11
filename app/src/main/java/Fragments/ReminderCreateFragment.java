package Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.hey.R;

import java.io.Serializable;

import Models.ListReminder;

public class ReminderCreateFragment extends Fragment
{
    private static final String TITLE = "Lời nhắc mới";
    private TextView addBtn,cancelBtn,titleTv;
    private EditText headline, note;
    private Dialog d;
    private ListReminder listChoosed;
    private ImageView colorListChoosed;
    private TextView nameListChoosed;
    private CardView listChoose;
    private CardView reminderDetail;



    public ReminderCreateFragment()
    {

    }

    public ReminderCreateFragment(Dialog d) {
        this.d = d;
    }

    public static ReminderCreateFragment newInstance()
    {
        Bundle args = new Bundle();
        ReminderCreateFragment fragment = new ReminderCreateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReminderCreateFragment newInstance(Dialog d)
    {
        Bundle args = new Bundle();
        ReminderCreateFragment fragment = new ReminderCreateFragment(d);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reminder_create,container,false);
        initComponent(v);
        initClickEvent();
        setTitle();
        return v;
    }

    private void initClickEvent()
    {

        addBtn.setOnClickListener(v -> {

        });

        cancelBtn.setOnClickListener(v -> {
            d.dismiss();
        });

        listChoose.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left,0 ,0, android.R.anim.slide_out_right)
                    .replace(R.id.fragment_container,ListChooseFragment.newInstance(this))
                    .addToBackStack(null)
                    .commit();

        });

        reminderDetail.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left,0 ,0, android.R.anim.slide_out_right)
                    .replace(R.id.fragment_container,ReminderDetailFragment.newInstance(this))
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setTitle() {
        addBtn.setText("Thêm");
        titleTv.setText(TITLE);
    }

    private void initComponent(View v)
    {
        addBtn = v.findViewById(R.id.add_bottom_sheet);
        cancelBtn = v.findViewById(R.id.cancel_bottom_sheet);
        titleTv = v.findViewById(R.id.header_bottom_sheet);
        listChoose = v.findViewById(R.id.list_choose);
        nameListChoosed = v.findViewById(R.id.list_choose_name);
        colorListChoosed = v.findViewById(R.id.icon_list_choose);
        reminderDetail = v.findViewById(R.id.reminderDetail);
        headline = v.findViewById(R.id.headline);
        note = v.findViewById(R.id.note);
    }

    public ListReminder getListChoosed() {
        return listChoosed;
    }

    public void setListChoosed(ListReminder listChoosed) {
        this.listChoosed = listChoosed;

    }

    private void updateFragmnent ()
    {
        if (listChoosed != null) {
            if (listChoosed.getColor() != 0) {
                colorListChoosed.getBackground().setTint(listChoosed.getColor());
            }
            nameListChoosed.setText(listChoosed.getListName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFragmnent();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
