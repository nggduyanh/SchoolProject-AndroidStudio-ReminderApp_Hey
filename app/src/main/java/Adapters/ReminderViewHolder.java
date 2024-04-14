package Adapters;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

public class ReminderViewHolder extends RecyclerView.ViewHolder {
    EditText reminderName;
    RadioButton radioButton;
    ImageView imageView;
    TextView date,time;


    public ReminderViewHolder(@NonNull View itemView){
        super(itemView);
        reminderName = itemView.findViewById(R.id.reminder_name);
        radioButton= itemView.findViewById(R.id.radioButton);
        imageView =itemView.findViewById(R.id.icon_info);
        date=itemView.findViewById(R.id.date);
        time=itemView.findViewById(R.id.time);
    }
}
