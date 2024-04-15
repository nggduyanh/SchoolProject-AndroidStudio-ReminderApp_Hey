package Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

import java.util.List;

public class PhotoReminderAdapter extends RecyclerView.Adapter<PhotoReminderViewHolder> {

    private List<Uri> images;

    public PhotoReminderAdapter(List<Uri> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public PhotoReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_reminder_detail,parent,false);
        return new PhotoReminderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoReminderViewHolder holder, int position)
    {
        Uri uri = images.get(position);
        holder.image.setImageURI(uri);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
