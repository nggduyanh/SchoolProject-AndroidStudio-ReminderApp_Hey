package Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Adapters.ListChooseAdapter;
import Adapters.ListViewHolder;
import Interfaces.IClickListChoose;
import Models.ListReminder;

public class ListChooseFragment extends Fragment {

    private RecyclerView listRV;
    private TextView cancelBtn, addBtn, titleTv;

    private ReminderCreateFragment siblingFragment;

    public ListChooseFragment() {
    }

    public ListChooseFragment(ReminderCreateFragment siblingFragment) {
        this.siblingFragment = siblingFragment;
    }

    public static ListChooseFragment newInstance() {

        Bundle args = new Bundle();
        ListChooseFragment fragment = new ListChooseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ListChooseFragment newInstance(ReminderCreateFragment siblingFragment) {

        Bundle args = new Bundle();
        ListChooseFragment fragment = new ListChooseFragment(siblingFragment);
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
        View v = inflater.inflate(R.layout.list_choose,null );
        initComponent(v);
        setTitle();
        initRecycleView();
        initClickEvent();
        return v;
    }

    private void initClickEvent() {

        cancelBtn.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

    }

    private void initComponent (View v)
    {
        listRV = v.findViewById(R.id.list_choose_rv);
        cancelBtn = v.findViewById(R.id.cancel_bottom_sheet);
        addBtn = v.findViewById(R.id.add_bottom_sheet);
        titleTv = v.findViewById(R.id.header_bottom_sheet);
    }

    private void setTitle()
    {
        cancelBtn.setText("Quay lại");
        titleTv.setText("Danh sách");
        addBtn.setVisibility(View.INVISIBLE);
        cancelBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.back_icon,0,0,0);
    }

    private void initRecycleView ()
    {
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        List<ListReminder> l  = new ArrayList<>(Arrays.asList(
            new ListReminder(1,"lmao",3),
            new ListReminder(2,"hihi",4),
            new ListReminder(3,"bobo",5),
            new ListReminder(3,"bobo",5),
            new ListReminder(2,"hihi",4),
            new ListReminder(3,"bobo",5),
            new ListReminder(3,"bobo",5),
            new ListReminder(2,"hihi",4),
            new ListReminder(3,"bobo",5)
        ));

        listRV.setLayoutManager(layout);
        ListChooseFragment that = this;
        ListChooseAdapter adapter = new ListChooseAdapter(l, new IClickListChoose() {
            @Override
            public void clickHandle(ListViewHolder holder,int position) {
                getParentFragmentManager().popBackStack();
                if (siblingFragment != null)
                {
                    Log.d("padz","bab");
                    siblingFragment.setListChoosed(l.get(position));
                }
                getParentFragmentManager().beginTransaction().remove(that).commit();
            }
        });
        adapter.setIdList(3);
        listRV.setAdapter(adapter);
    }

}
