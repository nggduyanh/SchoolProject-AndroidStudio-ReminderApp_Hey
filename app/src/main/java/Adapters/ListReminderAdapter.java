package Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;
import com.example.hey.ReminderActivity;

import java.util.List;

import Interfaces.ICallReminderActivity;
import Models.ListReminder;

public class ListReminderAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private int longClickPosition;
    private List <ListReminder> list;
    private ICallReminderActivity iCallReminderActivity;

    public ListReminderAdapter(List<ListReminder> list, ICallReminderActivity iCallReminderActivity) {
        this.list = list;
        this.iCallReminderActivity=iCallReminderActivity;
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
        holder.icon.setImageResource(obj.getIcon());
        holder.icon.getBackground().setTint(obj.getColor());
        holder.icon.setEnabled(false);
        holder.name.setText(obj.getListName());
        holder.number.setText("" + obj.getNumberReminder());

        holder.root.setOnClickListener(v-> {
                    iCallReminderActivity.getListPosition(position);
                    iCallReminderActivity.intentCall();
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickPosition = holder.getAdapterPosition();
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getLongClickPosition() {
        return longClickPosition;
    }

    public void setLongClickPosition(int longClickPosition) {
        this.longClickPosition = longClickPosition;
    }
}
