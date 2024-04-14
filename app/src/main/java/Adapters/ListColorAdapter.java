package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;
import com.google.android.flexbox.FlexboxLayoutManager;

import Interfaces.IClickListAdd;

public class ListColorAdapter extends RecyclerView.Adapter<ListCreateViewHolder> {

    private int [] listColorId;

    private IClickListAdd clickCallback;

    public ListColorAdapter(int[] listColorId, IClickListAdd clickCallback) {
        this.listColorId = listColorId;
        this.clickCallback = clickCallback;
    }

    @NonNull
    @Override
    public ListCreateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_icon_color_choose,parent,false);
        return new ListCreateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListCreateViewHolder holder, int position) {

        holder.color.getBackground().setTint(listColorId[position]);
        holder.color.setEnabled(false);
        if (position != 0) holder.outline.setVisibility(View.INVISIBLE);
        else {
            holder.outline.setVisibility(View.VISIBLE);
            clickCallback.setColor(holder);
        }
        holder.container.setOnClickListener(v -> {
            clickCallback.setEnable(holder,listColorId.length);
            clickCallback.setColor(holder);
        });


        FlexboxLayoutManager.LayoutParams lp = (FlexboxLayoutManager.LayoutParams) holder.container.getLayoutParams();
        lp.setFlexBasisPercent(0.16f);
        lp.height = lp.width;
    }

    @Override
    public int getItemCount() {
        return listColorId.length;
    }
}
