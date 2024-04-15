package Adapters;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    TextView name,number;
    ImageView icon;
    ConstraintLayout root;
    ImageView nextIcon;


    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name_list_item);
        number = itemView.findViewById(R.id.number_list_item);
        icon = itemView.findViewById(R.id.icon_list_item);
        nextIcon = itemView.findViewById(R.id.list_next_icon);
        root = itemView.findViewById(R.id.root_list_reminder_item);
        itemView.setOnCreateContextMenuListener(this);
    }

    public TextView getName() {
        return name;
    }

    public TextView getNumber() {
        return number;
    }

    public ImageView getIcon() {
        return icon;
    }

    public ImageView getNextIcon() {
        return nextIcon;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        new MenuInflater(v.getContext()).inflate(R.menu.context_menu_list_reminder,menu);
    }
}
