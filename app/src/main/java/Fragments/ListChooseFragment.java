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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Adapters.ListChooseAdapter;
import Adapters.ListViewHolder;
import Database.DbContext;
import Interfaces.IClickListChoose;
import ItemDecoration.ItemDivider;
import Models.ListReminder;

public class ListChooseFragment extends Fragment {

    private RecyclerView listRV;
    private TextView cancelBtn, addBtn, titleTv;
    private ReminderCreateFragment siblingFragment;

    private int idListChoosed;

    public ListChooseFragment() {
    }

    public ListChooseFragment(ReminderCreateFragment siblingFragment,int id) {
        this.siblingFragment = siblingFragment;
        this.idListChoosed = id;
    }

    public static ListChooseFragment newInstance() {

        Bundle args = new Bundle();
        ListChooseFragment fragment = new ListChooseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ListChooseFragment newInstance(ReminderCreateFragment siblingFragment,int ID) {

        Bundle args = new Bundle();
        ListChooseFragment fragment = new ListChooseFragment(siblingFragment,ID);
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
        List<ListReminder> l = DbContext.getInstance(getContext()).getListReminder();

        listRV.setLayoutManager(layout);
        ListChooseFragment that = this;
        ListChooseAdapter adapter = new ListChooseAdapter(l, new IClickListChoose() {
            @Override
            public void clickHandle(ListViewHolder holder,int position) {
                BottomSheetFragment parent = (BottomSheetFragment) that.getParentFragment();
                parent.getReminderInstance().setListReminder(l.get(position));
                if (siblingFragment != null)
                {
                    ListReminder obj = l.get(position);
                    siblingFragment.setListChoosed(obj);
                }
                getParentFragmentManager().popBackStack();
                getParentFragmentManager().beginTransaction().remove(that).commit();
            }
        });
        adapter.setIdList(idListChoosed);
        listRV.setAdapter(adapter);
        listRV.addItemDecoration(new ItemDivider(getContext(), DividerItemDecoration.VERTICAL));
    }

}
