package Fragments;

import android.app.Dialog;
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
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class ListChooseFragment extends Fragment {

    private RecyclerView listRV;
    private TextView cancelBtn, addBtn, titleTv;
    private ReminderCreateFragment siblingFragment;
    private int idListChoosed;
    private Dialog d;
    private BlurView headerBlur;

    public ListChooseFragment() {
    }

    public ListChooseFragment(ReminderCreateFragment siblingFragment,int id,Dialog d) {
        this.siblingFragment = siblingFragment;
        this.idListChoosed = id;
        this.d = d;
    }

    public static ListChooseFragment newInstance() {

        Bundle args = new Bundle();
        ListChooseFragment fragment = new ListChooseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ListChooseFragment newInstance(ReminderCreateFragment siblingFragment,int ID,Dialog d) {

        Bundle args = new Bundle();
        ListChooseFragment fragment = new ListChooseFragment(siblingFragment,ID,d);
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
        setUpBlur(v,d);
        return v;
    }

    private void setUpBlur(View view, Dialog dialog)
    {
        ViewGroup root = view.findViewById(R.id.root_list_choose);
        headerBlur.setupWith(root,new RenderScriptBlur(getContext()))
                .setFrameClearDrawable(dialog.getWindow().getDecorView().getBackground())
                .setBlurAutoUpdate(true)
                .setBlurRadius(20f);
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
        headerBlur = v.findViewById(R.id.headerBlur);
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
