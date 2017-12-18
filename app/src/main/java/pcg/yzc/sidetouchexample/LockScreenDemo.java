package pcg.yzc.sidetouchexample;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class LockScreenDemo {
    private boolean display;
    private boolean have = false;
    private double res;
    public Bitmap bg_empty, bg_L, bg_R;
    private DrawingView drawingView;
    LockScreenDemo(DrawingView drawingView_) {
        drawingView = drawingView_;
    }
    public void changeDisplay() {
        display = !display;
    }

    public void update(double res_) {
        res = res_;
    }

    public void draw(Canvas canvas) {
        if (!display)
            return;
        if (res < 0)
            canvas.drawBitmap(bg_L, 0, 0, drawingView.picPaint);
        else if (res > 0)
            canvas.drawBitmap(bg_R, 0, 0, drawingView.picPaint);
        else
            canvas.drawBitmap(bg_empty, 0, 0, drawingView.picPaint);
    }
}
