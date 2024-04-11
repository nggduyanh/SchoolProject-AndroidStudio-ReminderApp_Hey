package Adapters;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

public class ReminderViewHolder extends RecyclerView.ViewHolder {
    EditText reminderName;
    RadioButton radioButton;


    public ReminderViewHolder(@NonNull View itemView){
        super(itemView);
        reminderName = itemView.findViewById(R.id.reminder_name);
        radioButton= itemView.findViewById(R.id.radioButton);
    }
}
