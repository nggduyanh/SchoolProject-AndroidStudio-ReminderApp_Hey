package Adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

public class PhotoReminderViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    public PhotoReminderViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.photoReminder);
    }
}
