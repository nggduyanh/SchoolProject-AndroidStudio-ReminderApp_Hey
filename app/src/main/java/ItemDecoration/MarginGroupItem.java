package ItemDecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarginGroupItem extends RecyclerView.ItemDecoration {

    private int marginBottom, marginRight;

    public MarginGroupItem(int marginBottom, int marginRight) {
        this.marginBottom = marginBottom;
        this.marginRight = marginRight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = marginBottom;
        if (parent.getChildAdapterPosition(view) % 2 == 0 && parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)
        {
            outRect.right = marginRight;
        }
    }
}
