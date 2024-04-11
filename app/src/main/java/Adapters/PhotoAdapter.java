package Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

import java.util.List;

import Interfaces.IClickPhotoAdd;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {

    private List<Uri> photos;
    private IClickPhotoAdd click;

    public List<Uri> getPhotos() {
        return photos;
    }

    public PhotoAdapter(List<Uri> photos, IClickPhotoAdd click) {
        this.click = click;
        this.photos = photos;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position)
    {
        holder.imageChoose.setImageURI(photos.get(position));
        holder.deleteBtn.setOnClickListener(v -> {
            click.removePhoto(position);
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
