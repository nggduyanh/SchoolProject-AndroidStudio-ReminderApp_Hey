package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

import java.util.List;

import Interfaces.IClickListAdd;
import Interfaces.IClickListChoose;
import Models.ListReminder;

public class ListChooseAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private List<ListReminder> list;

    private int idList;

    private IClickListChoose Iclick;

    public ListChooseAdapter(List<ListReminder> list, IClickListChoose iclick) {
        this.list = list;
        this.Iclick = iclick;
    }

    public int getIdList() {
        return idList;
    }

    public void setIdList(int idList) {
        this.idList = idList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reminder_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListReminder obj = list.get(position);
        holder.icon.setEnabled(false);
        holder.icon.setImageResource(obj.getIcon());
        holder.icon.getBackground().setTint(obj.getColor());
        holder.number.setVisibility(View.INVISIBLE);
        holder.name.setText(obj.getListName());
        holder.nextIcon.setImageResource(R.drawable.icon_tick);
        holder.nextIcon.setPadding(0,0,20,0);
        if (idList == obj.getId()) holder.nextIcon.setVisibility(View.VISIBLE);
        else holder.nextIcon.setVisibility(View.INVISIBLE);
        holder.root.setOnClickListener(v -> {
            Iclick.clickHandle(holder,position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
