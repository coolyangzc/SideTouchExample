package pcg.yzc.sidetouchexample;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.StringBuilder;

public class DrawingView extends View implements Runnable {

    private Paint capaPaint = null;
    private MainActivity activity;
    private HandPostureDetector hDetector;
    int[][] capa = new int[6][Common.CapaNum_H]; //0~2: Left; 3~5: Right; 0~29: Up~Down

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (MainActivity) context;
        capaPaint = new Paint();
        hDetector = new HandPostureDetector();
        capaPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 6; ++i)
            for (int j = 0; j < Common.CapaNum_H; ++j) {
                if (capa[i][j] > 0)
                    capaPaint.setColor(Color.argb(capa[i][j],255, 0, 0));
                else
                    capaPaint.setColor(Color.WHITE);
                if (i < 3)
                    canvas.drawRect(Common.capa_W * i, Common.capa_H * j,
                            Common.capa_W * (i + 1), Common.capa_H * (j + 1), capaPaint);
                else
                    canvas.drawRect(Common.screen_W - Common.capa_W * (6 - i), Common.capa_H * j,
                            Common.screen_W - Common.capa_W * (6 - i - 1), Common.capa_H * (j + 1),
                            capaPaint);
            }
    }

    public void initialize() {
        Thread thread = new Thread(this);
        thread.start();
    }

    private String getString(String path) {
        String prop = "waiting";// 默认值
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String temp;
            while ((temp = reader.readLine()) != null) {
                sb.append(temp);
                sb.append("\n");
            }

            String capaColumns[] = sb.toString().split("\n");

            for (int index = 0; index < 6; index++) {
                String capaValues[] = capaColumns[index].split(",");
                for (int index2 = 0; index2 < capaValues.length; ++index2) {
                    capa[index][index2] = Integer.valueOf(capaValues[index2].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        int frameCount = 0;

        while (true) {
            getString("proc/touchscreen/tp_capacitance_data");
            postInvalidate();
            frameCount++;
            if (System.currentTimeMillis() > startTime && frameCount % 100 == 0)
                Log.d("frame", "frame = " + frameCount + ", frame rate = "
                        + (frameCount * 1000 / (System.currentTimeMillis() - startTime)));

            double res = hDetector.predict(capa);
            String s = "空\n";
            if (res < 0)
                s = "左\n";
            if (res > 0)
                s = "右\n";
            s += String.valueOf(res) + "\n" + String.valueOf(frameCount);
            activity.update(s);
        }
    }
}
