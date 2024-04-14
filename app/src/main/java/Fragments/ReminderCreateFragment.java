package Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.List;

import Database.DbContext;
import Interfaces.IUpdateDatabase;
import Models.ListReminder;
import Models.Reminder;

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
    private TextView dateTime;
    private String dateTimeString;

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        List <ListReminder> l = DbContext.getInstance(getContext()).getListReminder();
        listChoosed = l.size() == 0 ? null : l.get(0);
        BottomSheetFragment parent = (BottomSheetFragment) getParentFragment();
        parent.getReminderInstance().setListReminder(listChoosed);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reminder_create,container,false);
        initComponent(v);
        initEvent();
        setTitle();
        return v;
    }

    private void initEvent()
    {
        BottomSheetFragment parent = (BottomSheetFragment) getParentFragment();

        addBtn.setOnClickListener(v -> {
            Toast.makeText(getContext(), "d", Toast.LENGTH_SHORT).show();
            Reminder r = parent.getReminderInstance();
            DbContext.getInstance(getContext()).add(r);
            IUpdateDatabase fatherAct = (IUpdateDatabase) getActivity();
            fatherAct.updateInterface();
            d.dismiss();
        });

        cancelBtn.setOnClickListener(v -> {
            d.dismiss();
        });

        listChoose.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left,0 ,0, android.R.anim.slide_out_right)
                    .replace(R.id.fragment_container,ListChooseFragment.newInstance(this,listChoosed.getId()))
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

        headline.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                parent.getReminderInstance().setReminderName(headline.getText().toString());
                if (headline.getText().length() == 0) addBtn.setEnabled(false);
                else addBtn.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setTitle() {
        addBtn.setText("Thêm");
        titleTv.setText(TITLE);
        updateFragmnent();
    }

    private void initComponent(View v)
    {
        addBtn = v.findViewById(R.id.add_bottom_sheet);
        addBtn.setEnabled(false);
        cancelBtn = v.findViewById(R.id.cancel_bottom_sheet);
        titleTv = v.findViewById(R.id.header_bottom_sheet);
        listChoose = v.findViewById(R.id.list_choose);
        nameListChoosed = v.findViewById(R.id.list_choose_name);
        colorListChoosed = v.findViewById(R.id.icon_list_choose);
        colorListChoosed.setEnabled(false);
        reminderDetail = v.findViewById(R.id.reminderDetail);
        headline = v.findViewById(R.id.headline);
        note = v.findViewById(R.id.note);
        dateTime = v.findViewById(R.id.dateTime);
    }

    public ListReminder getListChoosed() {
        return listChoosed;
    }

    public void setListChoosed(ListReminder lmao) {
        this.listChoosed = lmao;
    }

    private void updateFragmnent ()
    {
        if (listChoosed != null)
        {
            if (listChoosed.getColor() != 0)
            {
                colorListChoosed.getBackground().setTint(listChoosed.getColor());
            }
            nameListChoosed.setText(listChoosed.getListName());
            BottomSheetFragment parent = (BottomSheetFragment) getParentFragment();
            parent.getReminderInstance().setListReminder(listChoosed);
        }

        if (dateTimeString == null || dateTimeString == "")
        {
            dateTime.setVisibility(View.GONE);
        }
        else
        {
            dateTime.setVisibility(View.VISIBLE);
            dateTime.setText(dateTimeString);
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

    public void setTextDateTime (String s)
    {
        dateTimeString = s;
    }



}
