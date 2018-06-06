package pcg.yzc.sidetouchexample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.SystemClock;

import java.util.LinkedList;
import java.util.Queue;

public class MovingScreenDemo extends AbstractDemo {
    public Bitmap bg;
    private DrawingView drawingView;
    private long lastTime, curTime;

    private Queue<Double>[] pos = new Queue[2];
    private Queue<Long>[] timestamps = new Queue[2];
    private int moving_edge = -1, last_edge = -1;
    private double delta = 0, screen_pos = 0;
    private double[] begin_pos = new double[2], last_pos = new double[2];
    private final int[] D = {0, 1};

    MovingScreenDemo(DrawingView drawingView_) {
        super();
        drawingView = drawingView_;
        lastTime = SystemClock.uptimeMillis();

        for (int i : D) {
            pos[i] = new LinkedList<Double>();
            timestamps[i] = new LinkedList<Long>();
        }
    }

    private void paint(Canvas canvas) {
        double result = screen_pos + delta * 120;
        result = Math.max(0, result);
        result = Math.min(500, result);
        if (moving_edge != -1)
            last_edge = moving_edge;
        int dx = -720;
        int dy = -2560;
        if (last_edge == 0)
            canvas.drawBitmap(bg, (float)-result + dx, (float)result*2 + dy, drawingView.picPaint);
        else
            canvas.drawBitmap(bg, (float)result + dx, (float)result*2 + dy, drawingView.picPaint);
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
                if (last_pos[i] != -1 && Math.abs(now_pos - last_pos[i]) > 4 || now_pos >= 18) {
                    continue;
                }
                pos[i].offer(now_pos);
                timestamps[i].offer(curTime);
                if (moving_edge == -1) {
                    if (Math.abs(now_pos - pos[i].peek()) > 2) {
                        begin_pos[i] = now_pos;
                        moving_edge = i;
                    }
                }
                if (moving_edge == i)
                    delta = res.highest_gravity[i] - begin_pos[i];
                last_pos[i] = now_pos;
            } else {
                pos[i].clear();
                timestamps[i].clear();
                screen_pos = screen_pos + delta * 120;
                screen_pos = Math.max(0, screen_pos);
                screen_pos = Math.min(500, screen_pos);
                delta = 0;
                moving_edge = -1;
                last_pos[i] = now_pos;
            }
        }

        /*
        if (now_pos != -1) {
            if (last_pos != -1 && Math.abs(now_pos - last_pos) > 4 || now_pos >= 18) {
                paint(canvas);
                return;
            }

            pos[i].offer(now_pos);
            timestamps.offer(curTime);
            if (!moving) {
                if (Math.abs(now_pos - pos[i].peek()) > 2) {
                    begin_pos = now_pos;
                    moving = true;
                }
            }
            if (moving)
                delta = res.highest_gravity[i] - begin_pos;
        } else {
            pos[i].clear();
            timestamps.clear();
            screen_pos = screen_pos + delta * 120;
            screen_pos = Math.max(0, screen_pos);
            screen_pos = Math.min(500, screen_pos);
            delta = 0;
            moving = false;
        }
        paint(canvas);
        last_pos = now_pos;*/
        paint(canvas);
    }
}
