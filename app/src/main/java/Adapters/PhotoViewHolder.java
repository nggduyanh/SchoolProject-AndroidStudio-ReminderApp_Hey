package Adapters;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hey.R;

public class PhotoViewHolder extends RecyclerView.ViewHolder {


    ImageButton deleteBtn;
    ImageButton shuffleBtn;
    ImageView imageChoose;


    public PhotoViewHolder(@NonNull View itemView) {
        super(itemView);
        deleteBtn = itemView.findViewById(R.id.deleteImage);
        imageChoose = itemView.findViewById(R.id.imageChoose);
    }
}
