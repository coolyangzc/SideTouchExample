package pcg.yzc.sidetouchexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.os.Vibrator;

import java.util.LinkedList;
import java.util.Queue;

public class MovingScreenDemo extends AbstractDemo {
    public Bitmap bg;
    private DrawingView drawingView;
    private long curTime;
    private Vibrator vibrator;

    private Queue<Double>[] pos = new Queue[2];
    private Queue<Long>[] timestamps = new Queue[2];
    private int moving_edge = -1, moving_dir = -1, last_edge = -1, last_dir = -1;
    private double delta = 0, screen_pos = 0, smooth_result = 0;
    private double[] begin_pos = new double[2], last_pos = new double[2];
    private final int[] D = {0, 1};

    private final int DX = -720, DY = -2136;

    MovingScreenDemo(DrawingView drawingView_, Context ctx_) {
        super();
        drawingView = drawingView_;
        vibrator = (Vibrator)ctx_.getSystemService(ctx_.VIBRATOR_SERVICE);

        for (int i : D) {
            pos[i] = new LinkedList<Double>();
            timestamps[i] = new LinkedList<Long>();
        }
    }

    private double trimPos(double pos) {
        if (moving_dir == 1) {
            pos = Math.max(0, pos);
            pos = Math.min(500, pos);
        } else {
            pos = Math.min(0, pos);
            pos = Math.max(-260, pos);
        }
        return pos;
    }

    private void paint(Canvas canvas) {
        if (moving_edge != -1) {
            if (last_edge != moving_edge && last_dir != -1)
                moving_dir = last_dir;
            last_edge = moving_edge;
            last_dir = moving_dir;
        }

        double result = trimPos(screen_pos + delta * 120);

        if (Math.abs(smooth_result - result) <= 6)
            result = smooth_result;
        else {
            if (result == 0)
                vibrator.vibrate(16);
            smooth_result = result;
        }

        if (last_edge == 0)
            if (moving_dir == 1)
                canvas.drawBitmap(bg, (float)-result + DX, (float)result*2 + DY, drawingView.picPaint);
            else
                canvas.drawBitmap(bg, (float)result*2 + DX, (float)result + DY, drawingView.picPaint);
        else
            if (moving_dir == 1)
                canvas.drawBitmap(bg, (float)result + DX, (float)result*2 + DY, drawingView.picPaint);
            else
                canvas.drawBitmap(bg, (float)-result*2 + DX, (float)result + DY, drawingView.picPaint);
    }

    public void draw(Canvas canvas) {
        if (!display)
            return;
        curTime = SystemClock.uptimeMillis();
        for(int i : D) {
            while (!timestamps[i].isEmpty() && curTime - timestamps[i].peek() > 500) {
                pos[i].poll();
                timestamps[i].poll();
            }
        }

        for(int i : D) {
            double now_pos = res.highest_gravity[i];
            if (moving_edge != -1 && moving_edge != i) {
                last_pos[i] = now_pos;
                continue;
            }
            if (now_pos != -1) {
                if (last_pos[i] != -1 && Math.abs(now_pos - last_pos[i]) > 4 || now_pos >= 19) {
                    continue;
                }
                pos[i].offer(now_pos);
                timestamps[i].offer(curTime);
                if (moving_edge == -1) {
                    if (Math.abs(now_pos - pos[i].peek()) > 2) {
                        begin_pos[i] = now_pos;
                        moving_edge = i;
                        if (moving_dir == -1)
                            if (now_pos > pos[i].peek())
                                moving_dir = 1;
                            else
                                moving_dir = 0;
                    }
                }
                if (moving_edge == i)
                    delta = res.highest_gravity[i] - begin_pos[i];
                last_pos[i] = now_pos;
            } else {
                pos[i].clear();
                timestamps[i].clear();
                screen_pos = screen_pos + delta * 120;
                screen_pos = trimPos(screen_pos);
                if (screen_pos == 0)
                    moving_dir = -1;
                delta = 0;
                moving_edge = -1;
                last_pos[i] = now_pos;
            }
        }

        paint(canvas);
    }
}
