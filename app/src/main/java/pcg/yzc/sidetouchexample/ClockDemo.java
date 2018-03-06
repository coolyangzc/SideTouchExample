package pcg.yzc.sidetouchexample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ClockDemo extends AbstractDemo{
    private DrawingView drawingView;
    private Paint paint;
    public Bitmap bg_clock;

    ClockDemo(DrawingView drawingView_) {
        super();
        drawingView = drawingView_;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(108);
    }

    public void draw(Canvas canvas) {
        if (!display)
            return;
        canvas.drawText("Grip:" + Boolean.toString(res.grip), Common.screen_W / 2 - 200, 400, paint);
        canvas.drawBitmap(bg_clock, Common.screen_W / 2 - 100, 600, drawingView.picPaint);
    }
}
