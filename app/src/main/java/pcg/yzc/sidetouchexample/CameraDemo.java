package pcg.yzc.sidetouchexample;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class CameraDemo extends AbstractDemo {
    private DrawingView drawingView;
    private Paint paint;

    CameraDemo(DrawingView drawingView_) {
        super();
        drawingView = drawingView_;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);
    }

    public void draw(Canvas canvas) {
        if (!display)
            return;
        if (res.U > res.D + 0.1f)
            canvas.drawCircle(Common.screen_W / 2, 400, 150, paint);
        else
            canvas.drawCircle(Common.screen_W / 2, Common.screen_H - 350, 150, paint);
    }
}
