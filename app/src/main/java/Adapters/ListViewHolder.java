package Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

public class ListViewHolder extends RecyclerView.ViewHolder {

    TextView name,number;
    ImageView icon;


    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name_list_item);
        number = itemView.findViewById(R.id.number_list_item);
        icon = itemView.findViewById(R.id.icon_list_item);
    }
}
