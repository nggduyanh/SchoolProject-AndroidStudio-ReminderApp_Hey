package Fragments;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hey.MainActivity;
import com.example.hey.R;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import Adapters.ListColorAdapter;
import Adapters.ListCreateViewHolder;
import Adapters.ListIconAdapter;
import Interfaces.IClickListAdd;
import Models.ListReminder;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomSheetListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomSheetListFragment extends BottomSheetDialogFragment implements IClickListAdd {

    private final String title = "Danh sách mới";
    private TextView cancelButton, addButton, titleTextView;
    private RecyclerView colorRV, iconRV;
    private ImageView iconResult;
    private ImageButton deleteBtn;
    private EditText listName;
    private int[] arrColor;
    private int[] arrIcon;
    private BlurView headerBlur;

    public BottomSheetListFragment()
    {

    }

    public static BottomSheetListFragment newInstance() {
        BottomSheetListFragment fragment = new BottomSheetListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_create,null);
        dialog.setContentView(view);

        initComponentBottomSheetGroup(view);
        setTitleBottomSheet();
        setOnclickComponent(dialog);
        initArrayResource ();
        initColorRecycleView();
        initIconRecycleView();
        initSearchEvent();
//        setUpBlur(view,dialog);
        return dialog;
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
            ListReminder newListReminder = new ListReminder(1,listName.getText().toString(),100);
            MainActivity father = (MainActivity) getActivity();
            father.addListReminder(newListReminder);
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
    public void setEnable(ListCreateViewHolder holder,int length) {
        for (int i=0; i < length;i++)
        {
            RecyclerView parent = (RecyclerView) holder.itemView.getParent();
            ListCreateViewHolder vh = (ListCreateViewHolder) parent.findViewHolderForAdapterPosition(i);
            vh.getOutline().setVisibility(View.INVISIBLE);
        }
        holder.getOutline().setVisibility(View.VISIBLE);
    }

    @Override
    public void setColor(ListCreateViewHolder holder)
    {
        int pos = colorRV.getChildLayoutPosition(holder.itemView);
        iconResult.getBackground().setTint(arrColor[pos]);
        listName.setTextColor(arrColor[pos]);
    }

    @Override
    public void setIcon(ListCreateViewHolder holder)
    {
        int pos = iconRV.getChildLayoutPosition(holder.itemView);
        iconResult.setImageResource(arrIcon[pos]);
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
                    addButton.setTextColor(ContextCompat.getColor(getContext(),R.color.blue));
                }
                else
                {
                    deleteBtn.setVisibility(View.INVISIBLE);
                    addButton.setTextColor(ContextCompat.getColor(getContext(),R.color.grey));
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