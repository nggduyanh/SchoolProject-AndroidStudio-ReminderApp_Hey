package Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.hey.R;

public class CornerView extends FrameLayout {
    private Path path = new Path();
    private float radius = 0;

    public CornerView(Context context) {
        super(context);
    }

    public CornerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CornerView);

        try {
            radius = a.getDimension(R.styleable.CornerView_CornerView_radius,0);

        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        RectF rect = new RectF();

        path.reset();
        rect.set(0, 0, w, h);
        path.addRoundRect(rect,new float[] {41,41,41,41,0,0,0,0}, Path.Direction.CW);
        path.close();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(path);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(save);
    }

}