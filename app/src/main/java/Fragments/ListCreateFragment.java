package Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.MainActivity;
import com.example.hey.R;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import Adapters.ListColorAdapter;
import Adapters.ListCreateViewHolder;
import Adapters.ListIconAdapter;
import Database.DbContext;
import Interfaces.IClickListAdd;
import Interfaces.IUpdateDatabase;
import Models.ListReminder;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class ListCreateFragment extends Fragment implements IClickListAdd {

    private final String title = "Danh sách mới";
    private TextView cancelButton, addButton, titleTextView;
    private RecyclerView colorRV, iconRV;
    private ImageView iconResult;
    private ImageButton deleteBtn;
    private EditText listName;
    private int[] arrColor;
    private int[] arrIcon;
    private BlurView headerBlur;
    private Dialog dialogParent;
    private int iconChoosed, colorChoosed;
    private BottomSheetFragment parent;

    public ListCreateFragment()
    {

    }

    public ListCreateFragment(Dialog dialogParent) {
        this.dialogParent = dialogParent;
    }

    public static ListCreateFragment newInstance(Dialog d) {
        ListCreateFragment fragment = new ListCreateFragment(d);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_create,container,false);
        initComponentBottomSheetGroup(v);
        setTitleBottomSheet();
        setOnclickComponent((BottomSheetDialog) dialogParent);
        initArrayResource ();
        initColorRecycleView();
        initIconRecycleView();
        initSearchEvent();
        setUpBlur(v,dialogParent);
        return v;
    }

    private void setUpBlur(View view,Dialog dialog)
    {
        ViewGroup root = view.findViewById(R.id.bottom_sheet_list_root);
        headerBlur.setupWith(root,new RenderScriptBlur(getContext()))
                .setFrameClearDrawable(dialog.getWindow().getDecorView().getBackground())
                .setBlurAutoUpdate(true)
                .setBlurRadius(20f);
    }

    private void initArrayResource() {
        arrColor = new int [] {
                ContextCompat.getColor(getContext(),R.color.red),
                ContextCompat.getColor(getContext(),R.color.blue),
                ContextCompat.getColor(getContext(),R.color.purple),
                ContextCompat.getColor(getContext(),R.color.green),
                ContextCompat.getColor(getContext(),R.color.aqua),
                ContextCompat.getColor(getContext(),R.color.crimson),
                ContextCompat.getColor(getContext(),R.color.peach),
                ContextCompat.getColor(getContext(),R.color.yellow),
                ContextCompat.getColor(getContext(),R.color.orange),
                ContextCompat.getColor(getContext(),R.color.darkYellow2),
                ContextCompat.getColor(getContext(),R.color.darkGrey2),
                ContextCompat.getColor(getContext(),R.color.tan)
        };

        arrIcon = new int[] {
                R.drawable.home_icon,
                R.drawable.pin_icon,
                R.drawable.icon_chat,
                R.drawable.icon_file,
                R.drawable.icon_med,
                R.drawable.icon_chair,
                R.drawable.icon_music,
                R.drawable.icon_game,
                R.drawable.icon_person,
                R.drawable.icon_bulb,
        };
    }

    private Drawable[] initDrawable (int []arr)
    {
        Drawable [] res = new Drawable[arr.length];
        for (int i=0; i< arr.length;i++)
        {
            res[i] = ContextCompat.getDrawable(getContext(),arr[i]);
        }
        return res;
    }

    private void initComponentBottomSheetGroup(View view)
    {
        cancelButton = view.findViewById(R.id.cancel_bottom_sheet);
        addButton = view.findViewById(R.id.add_bottom_sheet);
        titleTextView = view.findViewById(R.id.header_bottom_sheet);
        colorRV = view.findViewById(R.id.list_color);
        iconRV = view.findViewById(R.id.list_icon);
        iconResult = view.findViewById(R.id.icon_result);
        iconResult.setEnabled(false);
        listName = view.findViewById(R.id.list_name);
        deleteBtn = view.findViewById(R.id.delete_btn);
        headerBlur = view.findViewById(R.id.headerBlur);
        parent = (BottomSheetFragment) getParentFragment();
    }

    private void setTitleBottomSheet ()
    {
        titleTextView.setText(title);

        if (parent.getMode() == BottomSheetFragment.LIST_UPDATE) {
            addButton.setText("Xong");
            addButton.setEnabled(true );
            titleTextView.setText("Thông tin danh sách");
            listName.setText(parent.getListReminderInstance().getListName());
        }
    }

    private void setOnclickComponent (BottomSheetDialog dialog)
    {
        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        addButton.setOnClickListener(v -> {

            if (parent.getMode() == BottomSheetFragment.LIST_CREATE )
            {
                ListReminder newListReminder = new ListReminder(listName.getText().toString(),iconChoosed,colorChoosed);
                DbContext.getInstance(getContext()).add(newListReminder);
            }
            else if (parent.getMode() == BottomSheetFragment.LIST_UPDATE)
            {
                parent.getListReminderInstance().setListName(listName.getText().toString());
                parent.getListReminderInstance().setIcon(iconChoosed);
                parent.getListReminderInstance().setColor(colorChoosed);
                DbContext.getInstance(getContext()).update(parent.getListReminderInstance());
            }
            IUpdateDatabase father = (IUpdateDatabase) getActivity();
            father.updateInterface();
            dialog.dismiss();

        });
    }

    private void initColorRecycleView ()
    {
        FlexboxLayoutManager layout = new FlexboxLayoutManager(getContext());
        layout.setFlexDirection(FlexDirection.ROW);
        layout.setFlexWrap(FlexWrap.WRAP);
        layout.setJustifyContent(JustifyContent.CENTER);
        layout.setAlignItems(AlignItems.CENTER);
        colorRV.setLayoutManager(layout);
        ListColorAdapter adapter = new ListColorAdapter(arrColor, this);
        if (parent.getMode() == BottomSheetFragment.LIST_UPDATE)
        {
            iconResult.getBackground().setTint(parent.getListReminderInstance().getColor());
            listName.setTextColor(parent.getListReminderInstance().getColor());
            for (int i = 0 ; i < arrColor.length;i++)
            {
                if (arrColor[i] == parent.getListReminderInstance().getColor())
                {
                    adapter.setPrePosition(i);
                    break;
                }
            }
        }
        colorRV.setAdapter(adapter);
    }

    private void initIconRecycleView()
    {
        FlexboxLayoutManager layout = new FlexboxLayoutManager(getContext());
        layout.setFlexDirection(FlexDirection.ROW);
        layout.setFlexWrap(FlexWrap.WRAP);
        layout.setJustifyContent(JustifyContent.CENTER);
        layout.setAlignItems(AlignItems.CENTER);
        ListIconAdapter adapter = new ListIconAdapter(initDrawable(arrIcon), this);
        if (parent.getMode() == BottomSheetFragment.LIST_UPDATE)
        {
            for (int i =0 ; i < arrIcon.length;i++)
            {
                if (arrIcon[i] == parent.getListReminderInstance().getIcon())
                {
                    adapter.setPrePosition(i);
                    break;
                }
            }
        }
        iconRV.setLayoutManager(layout);
        iconRV.setAdapter(adapter);
    }

    @Override
    public void setEnable(ListCreateViewHolder holder, int length) {
        RecyclerView parent = (RecyclerView) holder.itemView.getParent();
        for (int i=0; i < length;i++)
        {
            ListCreateViewHolder vh = (ListCreateViewHolder) parent.findViewHolderForAdapterPosition(i);
            vh.getOutline().setVisibility(View.INVISIBLE);
        }
        holder.getOutline().setVisibility(View.VISIBLE);
    }

    @Override
    public void setColor(int pos)
    {
//        int pos = colorRV.getChildLayoutPosition(holder.itemView);
//        if (pos == -1) pos = 0;
        iconResult.getBackground().setTint(arrColor[pos]);
        listName.setTextColor(arrColor[pos]);
        colorChoosed = arrColor[pos];
    }

    @Override
    public void setIcon(int pos)
    {
//        int pos = iconRV.getChildLayoutPosition(holder.itemView);
//        if (pos == -1 ) pos = 0;
        iconResult.setImageResource(arrIcon[pos]);
        iconChoosed = arrIcon[pos];
    }

    private void initSearchEvent ()
    {
        listName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (listName.getText().length() > 0)
                {
                    deleteBtn.setVisibility(View.VISIBLE);
                    addButton.setEnabled(true);
                }
                else
                {
                    deleteBtn.setVisibility(View.INVISIBLE);
                    addButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        deleteBtn.setOnClickListener(v -> {
            listName.setText("");
        });
    }

}
