package Adapters;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

import java.util.List;

import Models.ListReminder;

public class ListReminderAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private List <ListReminder> list;

    public ListReminderAdapter(List<ListReminder> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reminder_item,parent,false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListReminder obj = list.get(position);
        holder.name.setText(obj.getListName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
