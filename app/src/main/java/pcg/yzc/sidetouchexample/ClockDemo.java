package pcg.yzc.sidetouchexample;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ClockDemo extends AbstractDemo{
    private DrawingView drawingView;
    private Paint paint;

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
        //canvas.drawCircle(Common.screen_W / 2, 400, 150, paint);
        canvas.drawText("Test", Common.screen_W / 2 - 100, 400, paint);
        //if (res.U > res.D + 0.1f)
            //canvas.drawCircle(Common.screen_W / 2, 400, 150, paint);
        //else
            //anvas.drawCircle(Common.screen_W / 2, Common.screen_H - 350, 150, paint);
    }
}
