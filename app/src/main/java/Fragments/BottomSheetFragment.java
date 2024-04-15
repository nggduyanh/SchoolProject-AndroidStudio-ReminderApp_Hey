package Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hey.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import Models.ListReminder;
import Models.Reminder;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    public static final int LIST_UPDATE = 5;
    private int mode;
    public static final int LIST_CREATE = 1;
    public static final int REMINDER_CREATE = 2;
    private Reminder reminder;
    private ListReminder listReminder;

    public BottomSheetFragment()
    {

    }

    public BottomSheetFragment(int mode) {
        this.mode = mode;
    }

    public static BottomSheetFragment newInstance(int mode) {
        BottomSheetFragment fragment = new BottomSheetFragment(mode);
        return fragment;
    }

    public static BottomSheetFragment newInstance (int mode, ListReminder lr)
    {
        BottomSheetFragment fragment = new BottomSheetFragment(mode);
        fragment.setListReminder(lr);
        return fragment;
    }

    private void setListReminder(ListReminder listReminder) {
        if (mode == LIST_UPDATE) this.listReminder = listReminder;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet,null);
        dialog.setContentView(view);

        switch (mode)
        {
            case LIST_CREATE:
            {
                getChildFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container, ListCreateFragment.newInstance(dialog),null)
                        .commit();
            }
            break;
            case REMINDER_CREATE:
            {
                getChildFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container,ReminderCreateFragment.newInstance(dialog),null)
                        .commit();
            }
            break;
            case LIST_UPDATE:
            {
                getChildFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container,ListCreateFragment.newInstance(dialog),null)
                        .commit();
            }
            break;
        }
        return dialog;
    }

    public Reminder getReminderInstance ()
    {
        if (reminder == null) reminder = new Reminder();
        return reminder;
    }

    public ListReminder getListReminderInstance ()
    {
        if (listReminder == null) listReminder = new ListReminder();
        return listReminder;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
