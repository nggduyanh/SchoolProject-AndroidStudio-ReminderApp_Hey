package Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hey.R;
import com.example.hey.ReminderActivity;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    private int mode;

    public static final int PhanAnh_MODE = 1;
    public static final int DuyAnh_MODE=2;
    private TextView addGroup,addReminder;
    private Activity activity;


    public AddFragment()
    {
    }

    public AddFragment(int mode,Activity reminderActivity){
        this.mode =mode;
        this.activity = reminderActivity;
    }

    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        return fragment;
    }

    public static AddFragment newInstance(int mode,Activity r) {

        AddFragment fragment = new AddFragment(mode,r);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add, container, false);
        initFragmentComponent(v);
        return v;
    }

    private void initFragmentComponent(View v)
    {
        addGroup = v.findViewById(R.id.addGroup);
        addReminder = v.findViewById(R.id.addReminder);

        if (mode == DuyAnh_MODE) addGroup.setVisibility(View.GONE);

        addGroup.setOnClickListener(view ->
        {
            BottomSheetFragment groupFragment = BottomSheetFragment.newInstance(BottomSheetFragment.LIST_CREATE);
            groupFragment.show(getChildFragmentManager(), groupFragment.getTag());
        });

        addReminder.setOnClickListener(view ->
        {
            if (mode == PhanAnh_MODE)
            {
                BottomSheetFragment groupFragment = BottomSheetFragment.newInstance(BottomSheetFragment.REMINDER_CREATE);
                groupFragment.show(getChildFragmentManager(), groupFragment.getTag());
            }
            else
            {
                ReminderActivity act = (ReminderActivity) activity;
                act.addReminder();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("phananh","" + mode);
    }
}