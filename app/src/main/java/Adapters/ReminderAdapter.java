package Adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

import java.util.List;

import Database.DbContext;
import Interfaces.IClickReminderInfo;
import Interfaces.ICreateNotification;
import Interfaces.IUpdateDatabase;
import Models.Reminder;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder> {


    private List<Reminder> list;
    private IClickReminderInfo iClickReminderInfo;
    private IUpdateDatabase iUpdateDatabase;


    public ReminderAdapter(List<Reminder> list, IClickReminderInfo iClickReminderInfo, ICreateNotification iCreateNotification, IUpdateDatabase iUpdateDatabase){
        this.list=list;
        this.iClickReminderInfo=iClickReminderInfo;
        this.iUpdateDatabase=iUpdateDatabase;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item,parent,false);
        return new ReminderViewHolder(v);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder model = list.get(position);
        Log.d("flag:", ""+ model.getFlag());
        if(model.getFlag()==true){
            holder.reminderName.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.icon_flag,0);
        }
        else  holder.reminderName.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);

        if(model.getDate()==null){
            holder.date.setVisibility(View.GONE);
        }
        else holder.date.setText(model.getDate().toString());

        if(model.getTime()==null){
            holder.time.setVisibility(View.GONE);
        }
        else holder.time.setText(model.getTime().toString());

        if(model.getImage()!=null){
            Log.d("paDebug",model.getImage().size() + "");
            PhotoReminderAdapter adapter = new PhotoReminderAdapter(model.getImage());
            LinearLayoutManager layout = new LinearLayoutManager(holder.itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
            holder.imageContain.setLayoutManager(layout);
            holder.imageContain.setAdapter(adapter);
        }
        holder.radioButton.setChecked(model.getStatus());

        holder.reminderName.setText(model.getReminderName());

        holder.radioButton.setOnClickListener(v->{
            model.setStatus(!model.getStatus());
            holder.radioButton.setChecked(model.getStatus());
            DbContext.getInstance(v.getContext()).update(model);
        });

        holder.reminderName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus &&  model.getListReminder() != null){
                    model.setReminderName(holder.reminderName.getText().toString());
                    DbContext.getInstance(v.getContext()).update(model);
                    Log.d("check up", ""+model.getReminderName());
                }
            }
        });

        holder.imageOption.setOnClickListener(v->{
            iClickReminderInfo.clickReminderInfo(position);
            Log.d("checckck","sass");
//            Log.d("ten", model.getReminderName());
            if(model.getDate()==null){
                holder.date.setVisibility(View.GONE);
            }
            else holder.date.setVisibility(View.VISIBLE);

            if(model.getTime()==null){
                holder.time.setVisibility(View.GONE);
            }
            else holder.time.setVisibility(View.VISIBLE);
        });

        holder.imageDelete.setOnClickListener(v->{
            DbContext.getInstance(v.getContext()).delete(model);
            iUpdateDatabase.updateInterface();
        });

    }

    public List<Reminder> getList() {
        return list;
    }

    public void setList(List<Reminder> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
