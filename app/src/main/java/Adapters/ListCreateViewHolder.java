package Adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hey.R;

public class ListCreateViewHolder extends RecyclerView.ViewHolder {

    View color;
    View container;
    View icon;
    View outline;

    public ListCreateViewHolder(@NonNull View itemView) {
        super(itemView);
        initListColorComponent(itemView);
    }

    private void initListColorComponent(View itemView) {
        color = itemView.findViewById(R.id.color_choose);
        container = itemView.findViewById(R.id.choose_parent);
        icon = itemView.findViewById(R.id.icon_choose);
        outline = itemView.findViewById(R.id.choose_outline);
    }

    public View getColor() {
        return color;
    }
    public View getContainer() {
        return container;
    }
    public View getIcon() {
        return icon;
    }
    public View getOutline() {
        return outline;
    }
}
