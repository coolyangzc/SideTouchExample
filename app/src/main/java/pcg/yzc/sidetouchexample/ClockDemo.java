package pcg.yzc.sidetouchexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.widget.Toast;

public class ClockDemo extends AbstractDemo{
    private DrawingView drawingView;
    private Paint paint;
    private long lastTime;
    private boolean ringing;
    private Context ctx;
    public Bitmap bg_clock;

    ClockDemo(DrawingView drawingView_, Context ctx_) {
        super();
        drawingView = drawingView_;
        ctx = ctx_;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(108);
        ringing = true;
        lastTime = SystemClock.uptimeMillis();
    }

    public void draw(Canvas canvas) {
        if (!display)
            return;
        canvas.drawText("Grip:" + Boolean.toString(res.grip), Common.screen_W / 2 - 200, 400, paint);

        long curTime = SystemClock.uptimeMillis();
        if (ringing){
            if (res.grip) {
                ringing = false;
                Toast.makeText(ctx, "闹铃已关闭\n闹铃将在5s后重新响起", Toast.LENGTH_SHORT).show();
                lastTime = curTime;
            }
            if ((curTime - lastTime) / 500 % 2 == 0)
                canvas.drawBitmap(bg_clock, Common.screen_W / 2 - 100, 600, drawingView.picPaint);
        } else {
            if (curTime - lastTime >= 5000) {
                ringing = true;
                lastTime = curTime;
            }
        }

    }
}
