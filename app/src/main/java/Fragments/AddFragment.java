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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private String mParam1;
    private String mParam2;
    private int mode;

    private TextView addGroup,addReminder;

    public AddFragment()
    {
    }

    public AddFragment(int mode){
        this.mode =mode;
    }

    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        return fragment;
    }

    public static AddFragment newInstance(int mode) {

        AddFragment fragment = new AddFragment(mode);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

        addGroup.setOnClickListener(view ->
        {
            if (mode == 1)
            {
                BottomSheetFragment groupFragment = BottomSheetFragment.newInstance(BottomSheetFragment.LIST_CREATE);
                groupFragment.show(getChildFragmentManager(), groupFragment.getTag());
            }
            else
            {

            }
        });

        addReminder.setOnClickListener(view ->
        {
            BottomSheetFragment groupFragment = BottomSheetFragment.newInstance(BottomSheetFragment.REMINDER_CREATE);
            groupFragment.show(getChildFragmentManager(), groupFragment.getTag());

        });
    }
}