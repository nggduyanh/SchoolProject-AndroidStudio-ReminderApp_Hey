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

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private int mode;
    private ViewPager2 viewPager;
    public static final int LIST_CREATE = 1;
    public static final int REMINDER_ADD=3;
    public static final int REMINDER_CREATE = 2;

    private FragmentContainerView container;


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
        container = view.findViewById(R.id.fragment_container);

        switch (mode)
        {
            case LIST_CREATE:
            {
                getChildFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragment_container, ListCreateFragment.newInstance(dialog),null).commit();
            }
            break;
            case REMINDER_CREATE:
            {
                getChildFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragment_container,ReminderCreateFragment.newInstance(dialog),null).commit();
            }
            break;

        }
        return dialog;
    }
}
