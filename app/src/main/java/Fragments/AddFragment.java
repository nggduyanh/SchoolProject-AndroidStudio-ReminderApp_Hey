package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hey.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {
    private TextView addGroup,addReminder;

    public AddFragment()
    {

    }

    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        addGroup.setOnClickListener(view ->
        {
            BottomSheetFragment groupFragment = BottomSheetFragment.newInstance(BottomSheetFragment.LIST_CREATE);
            groupFragment.show(getChildFragmentManager(), groupFragment.getTag());
        });

        addReminder.setOnClickListener(view ->
        {
            BottomSheetFragment groupFragment = BottomSheetFragment.newInstance(BottomSheetFragment.REMINDER_CREATE);
            groupFragment.show(getChildFragmentManager(), groupFragment.getTag());
        });
    }
}