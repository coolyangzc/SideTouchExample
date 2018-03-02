package pcg.yzc.sidetouchexample;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ClockDemo extends AbstractDemo{
    private boolean display;
    private HandPostureResult res;
    private DrawingView drawingView;

    ClockDemo(DrawingView drawingView_) {
        drawingView = drawingView_;

    }

    public void draw(Canvas canvas) {
        if (!display)
            return;
        //if (res.U > res.D + 0.1f)
            //canvas.drawCircle(Common.screen_W / 2, 400, 150, paint);
        //else
            //anvas.drawCircle(Common.screen_W / 2, Common.screen_H - 350, 150, paint);
    }
}
