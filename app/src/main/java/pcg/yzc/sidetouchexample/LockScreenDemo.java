package pcg.yzc.sidetouchexample;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class LockScreenDemo extends AbstractDemo {
    public Bitmap bg_empty, bg_L, bg_R;
    private DrawingView drawingView;
    LockScreenDemo(DrawingView drawingView_) {
        super();
        drawingView = drawingView_;
    }

    public void draw(Canvas canvas) {
        if (!display)
            return;
        if (res.L > res.R + 0.2f)
            canvas.drawBitmap(bg_L, 0, 0, drawingView.picPaint);
        else if (res.R > res.L + 0.2f)
            canvas.drawBitmap(bg_R, 0, 0, drawingView.picPaint);
        else
            canvas.drawBitmap(bg_empty, 0, 0, drawingView.picPaint);
    }
}
