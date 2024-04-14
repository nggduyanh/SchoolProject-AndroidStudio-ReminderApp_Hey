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
                .setBlurRadius(20f);
    }

    private void initArrayResource() {
        arrColor = new int [] {
                ContextCompat.getColor(getContext(),R.color.red),
                ContextCompat.getColor(getContext(),R.color.blue),
                ContextCompat.getColor(getContext(),R.color.purple),
                ContextCompat.getColor(getContext(),R.color.green),
                ContextCompat.getColor(getContext(),R.color.cyan),
                ContextCompat.getColor(getContext(),R.color.crimson),
                ContextCompat.getColor(getContext(),R.color.peach),
                ContextCompat.getColor(getContext(),R.color.MillenialPink),
                ContextCompat.getColor(getContext(),R.color.blue),
                ContextCompat.getColor(getContext(),R.color.blue),
                ContextCompat.getColor(getContext(),R.color.blue),
                ContextCompat.getColor(getContext(),R.color.blue)
        };

        arrIcon = new int[] {
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search,
                R.drawable.delete_icon,
                R.drawable.icon_search
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
    }

    private void setTitleBottomSheet ()
    {
        titleTextView.setText(title);
    }

    private void setOnclickComponent (BottomSheetDialog dialog)
    {
        cancelButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        addButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "d", Toast.LENGTH_SHORT).show();
            ListReminder newListReminder = new ListReminder(listName.getText().toString(),iconChoosed,colorChoosed);
            IUpdateDatabase father = (IUpdateDatabase) getActivity();
            DbContext.getInstance(getContext()).add(newListReminder);
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
    public void setColor(ListCreateViewHolder holder)
    {
        int pos = colorRV.getChildLayoutPosition(holder.itemView);
        if (pos == -1) pos = 0;
        iconResult.getBackground().setTint(arrColor[pos]);
        listName.setTextColor(arrColor[pos]);
        colorChoosed = arrColor[pos];
    }

    @Override
    public void setIcon(ListCreateViewHolder holder)
    {
        int pos = iconRV.getChildLayoutPosition(holder.itemView);
        if (pos == -1 ) pos = 0;
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
