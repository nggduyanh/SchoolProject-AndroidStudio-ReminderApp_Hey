package Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hey.R;

public class GroupViewHolder extends RecyclerView.ViewHolder {

    ImageView iconGroupItem;

    TextView reminderNumber, headerGroupItem;

    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);
        initGroupItemComponents(itemView);
    }

    private void initGroupItemComponents(View v)
    {
        iconGroupItem =  v.findViewById(R.id.icon_group_item);
        reminderNumber = v.findViewById(R.id.number_group_item);
        headerGroupItem = v.findViewById(R.id.header_group_item);
    }
}
