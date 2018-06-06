package pcg.yzc.sidetouchexample;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class MovingScreenDemo extends AbstractDemo {
    public Bitmap bg;
    private DrawingView drawingView;

    MovingScreenDemo(DrawingView drawingView_) {
        super();
        drawingView = drawingView_;
    }

    public void draw(Canvas canvas) {
        if (!display)
            return;
        canvas.drawBitmap(bg, 0, 0, drawingView.picPaint);
    }
}
