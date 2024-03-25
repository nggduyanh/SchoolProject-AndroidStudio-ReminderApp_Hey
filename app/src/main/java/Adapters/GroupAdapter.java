package Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

import java.util.List;

import Models.Group;

public class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {

    private List<Group> groupList;

    public GroupAdapter(List<Group> groupList) {
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item,parent,false);

        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group model = groupList.get(position);

//        holder.iconGroupItem.setImageURI(model.getIcon());
        holder.headerGroupItem.setText(model.getName());
        holder.reminderNumber.setText(new Integer(model.getNumberReminder()).toString());
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
