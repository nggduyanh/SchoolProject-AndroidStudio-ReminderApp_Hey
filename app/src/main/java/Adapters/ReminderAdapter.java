package Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

import java.io.Console;
import java.util.List;

import Models.Reminder;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder> {

    private List<Reminder> list;

    public ReminderAdapter(List<Reminder> list){this.list=list;}

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item,parent,false);
        return new ReminderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder model = list.get(position);
        holder.reminderName.setText(model.getReminderName());
        holder.radioButton.setOnClickListener(v->{

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
