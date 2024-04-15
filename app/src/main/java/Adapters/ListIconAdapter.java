package Adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hey.R;
import com.google.android.flexbox.FlexboxLayoutManager;

import Interfaces.IClickListAdd;

public class ListIconAdapter extends RecyclerView.Adapter<ListCreateViewHolder>{

    private int prePosition;
    private Drawable[] iconDrawable;

    private IClickListAdd clickCallback;

    public ListIconAdapter(Drawable[] idIcon, IClickListAdd clickCallback) {
        this.iconDrawable = idIcon;
        this.clickCallback = clickCallback;
    }

    @NonNull
    @Override
    public ListCreateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListCreateViewHolder (LayoutInflater.from(parent.getContext()).inflate(R.layout.list_icon_color_choose,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListCreateViewHolder holder, int position) {
        holder.icon.setBackground(iconDrawable[position]);
        if (prePosition != position ) holder.outline.setVisibility(View.INVISIBLE);
        else
        {
            holder.outline.setVisibility(View.VISIBLE);
            clickCallback.setIcon(position);
        }
        holder.container.setOnClickListener(v -> {
            clickCallback.setEnable(holder,iconDrawable.length);
            clickCallback.setIcon(position);
        });
        FlexboxLayoutManager.LayoutParams lp = (FlexboxLayoutManager.LayoutParams) holder.container.getLayoutParams();
        lp.setFlexBasisPercent(0.16f);
        lp.height = lp.width;
    }

    @Override
    public int getItemCount() {
        return iconDrawable.length;
    }

    public int getPrePosition() {
        return prePosition;
    }

    public void setPrePosition(int prePosition) {
        this.prePosition = prePosition;
    }
}
