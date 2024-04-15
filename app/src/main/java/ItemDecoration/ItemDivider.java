package ItemDecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDivider extends DividerItemDecoration {
    public ItemDivider(Context context, int orientation) {
        super(context, orientation);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + 100;
        Log.d("padz",parent.getPaddingLeft() + "");
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + this.getDrawable().getIntrinsicHeight();

            getDrawable().setBounds(left, top, right, bottom);
            getDrawable().draw(c);
        }
    }
}
