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
        paint = new Paint(Color.WHITE);
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
        if (res.U > res.D + 0.2f)
            canvas.drawCircle(Common.screen_W / 2, 300, 100, paint);
        else
            canvas.drawCircle(Common.screen_W / 2, Common.screen_H - 300, 100, paint);
    }
}
