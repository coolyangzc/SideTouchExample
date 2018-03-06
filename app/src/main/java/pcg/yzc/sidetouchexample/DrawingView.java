package pcg.yzc.sidetouchexample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.ArrayList;

public class DrawingView extends View implements Runnable {

    public Paint capaPaint = null, picPaint = null;
    private MainActivity activity;
    private HandPostureDetector hDetector;
    public ArrayList<AbstractDemo> demos = new ArrayList<AbstractDemo>();

    public LockScreenDemo lockScreenDemo;
    public CameraDemo cameraDemo;
    public ClockDemo clockDemo;

    int[][] capa = new int[6][Common.CapaNum_H]; //0~2: Left; 3~5: Right; 0~29: Up~Down

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (MainActivity) context;
        hDetector = new HandPostureDetector();
        initialize();
    }

    private void initialize() {
        //Setup Paints
        capaPaint = new Paint();
        capaPaint.setStyle(Paint.Style.FILL);
        picPaint = new Paint();
        picPaint.setAntiAlias(true);
        picPaint.setFilterBitmap(true);
        picPaint.setColor(Color.WHITE);

        //Load Resources
        lockScreenDemo = new LockScreenDemo(this);
        cameraDemo = new CameraDemo(this);
        clockDemo = new ClockDemo(this);

        lockScreenDemo.bg_empty = BitmapFactory.decodeResource(getResources(), R.mipmap.sea_bg);
        lockScreenDemo.bg_L = BitmapFactory.decodeResource(getResources(), R.mipmap.sea_l);
        lockScreenDemo.bg_R = BitmapFactory.decodeResource(getResources(), R.mipmap.sea_r);

        clockDemo.bg_clock = BitmapFactory.decodeResource(getResources(), R.mipmap.clock);



        demos.add(lockScreenDemo);
        demos.add(cameraDemo);
        demos.add(clockDemo);

        //Start Thread
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        super.onDraw(canvas);
        if (!cameraDemo.isDisplay())
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
        for(AbstractDemo demo:demos)
            demo.draw(canvas);
    }

    public boolean onBackPressed() {
        boolean returnQuit = true;
        for (AbstractDemo demo:demos)
            if (demo.isDisplay()) {
                returnQuit = false;
                break;
            }
        if (!returnQuit)
            for (AbstractDemo demo:demos)
                demo.changeDisplay(false);
        return returnQuit;
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

            HandPostureResult res = hDetector.predict(capa);
            String s = "空\n";

            if (res.L > res.R + 0.2f)
                s = "左\n";
            else if (res.R > res.L + 0.2f)
                s = "右\n";
            else
                s = "无\n";
            s += String.format("%.1f %.1f %.1f %.1f\n", res.L, res.R, res.U, res.D);
            activity.update(s);
            for(AbstractDemo demo:demos)
                demo.update(res);
        }
    }
}
