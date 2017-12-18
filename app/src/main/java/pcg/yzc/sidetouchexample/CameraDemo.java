package pcg.yzc.sidetouchexample;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class CameraDemo {
    private boolean display;
    private HandPostureResult res;
    private DrawingView drawingView;
    private Paint paint;

    CameraDemo(DrawingView drawingView_) {
        drawingView = drawingView_;
        paint = new Paint(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    public void changeDisplay(boolean display_) {
        display = display_;
    }

    public boolean isDisplay() {
        return display;
    }

    public void update(HandPostureResult res_) {
        res = res_;
    }

    public void draw(Canvas canvas) {
        if (!display)
            return;
        if (res.U > res.D + 0.1f)
            canvas.drawCircle(Common.screen_W / 2, 400, 160, paint);
        else
            canvas.drawCircle(Common.screen_W / 2, Common.screen_H - 250, 160, paint);
    }
}
