package Adapters;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;
import com.example.hey.ReminderActivity;

import java.io.Console;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

import Fragments.AddFragment;
import Fragments.BottomSheetFragment;
import Interfaces.IClickReminderInfo;
import Interfaces.ICreateNotification;
import Models.Reminder;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder> {

    private Context context;
    private List<Reminder> list;
    private IClickReminderInfo iClickReminderInfo;
    private ICreateNotification iCreateNotification;

    public ReminderAdapter(List<Reminder> list,IClickReminderInfo iClickReminderInfo,ICreateNotification iCreateNotification){
        this.list=list;
        this.iClickReminderInfo=iClickReminderInfo;
        this.iCreateNotification=iCreateNotification;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item,parent,false);
        context = parent.getContext();
        return new ReminderViewHolder(v);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder model = list.get(position);

        holder.reminderName.setText(model.getReminderName());

        holder.radioButton.setOnClickListener(v->{
            model.setStatus(!model.getStatus());
            holder.radioButton.setChecked(model.getStatus());
        });

        holder.reminderName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){

                    model.setReminderName(holder.reminderName.getText().toString());

                    if(model.getTime()!=null){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            iCreateNotification.scheduleNotification(model);
                            Log.d("noti","success");
                        }
                    }
                    Log.d("phahoia","he");
                }
            }
        });

        holder.imageView.setOnClickListener(v->{
            iClickReminderInfo.clickReminderInfo();
            Log.d("adapter", "onBindViewHolder: ");
            holder.reminderName.setCompoundDrawables(null,null,null,null);
            if(model.getDate()==null){
                holder.date.setVisibility(View.GONE);
            }
            else holder.date.setVisibility(View.VISIBLE);

            if(model.getTime()==null){
                holder.time.setVisibility(View.GONE);
            }
            else holder.time.setVisibility(View.VISIBLE);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
