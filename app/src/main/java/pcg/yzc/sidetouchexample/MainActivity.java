package pcg.yzc.sidetouchexample;

import android.Manifest;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DrawingView drawingView;
    static TextView textView_touchInfo, textView_resultInfo;
    private ArrayList<Button> btns = new ArrayList<Button>();
    public boolean debug_info = false;
    public TextHandler textHandler = new TextHandler();
    SurfaceView surfaceView;
    CameraView cameraView;

    OrientationEventListener mOrientationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Common.screen_W = metrics.widthPixels;
        Common.screen_H = metrics.heightPixels;

        Common.capa_W = Common.screen_W / Common.CapaNum_W;
        Common.capa_H = Common.screen_H / Common.CapaNum_H;

        drawingView = (DrawingView) findViewById(R.id.drawingView);
        textView_touchInfo = (TextView) findViewById(R.id.textView_touchInfo);
        textView_resultInfo = (TextView) findViewById(R.id.textView_resultInfo);

        Button btn_debug = findViewById(R.id.btn_debug);
        Button btn_lockScreen = findViewById(R.id.btn_lockScreen);
        Button btn_camera = findViewById(R.id.btn_camera);
        Button btn_clock = findViewById(R.id.btn_clock);
        Button btn_movingScreen = findViewById(R.id.btn_movingScreen);
        btns.add(btn_debug);
        btns.add(btn_lockScreen);
        btns.add(btn_camera);
        btns.add(btn_clock);
        btns.add(btn_movingScreen);

        for(Button btn:btns)
            btn.setOnClickListener(onClickListener);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        cameraView = new CameraView(surfaceView);

        mOrientationListener = new OrientationEventListener(this,
                SensorManager.SENSOR_DELAY_NORMAL) {

            @Override
            public void onOrientationChanged(int orientation) {
                Log.d("ori", "Orientation changed to " + orientation);
            }
        };
        mOrientationListener.enable();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        String text = "";
        for (int i = 0; i < event.getPointerCount(); ++i) {
            int id = event.getPointerId(i);
            int x = (int) event.getX(i);
            int y = (int) event.getY(i);
            int type = getType(event, i);
            text += "Id:" + id + " X:" + x + " Y:" + y + " Type:" + type + "\n";
        }
        if (!debug_info)
            text = "";
        textView_touchInfo.setText(text);
        if (event.getAction() == MotionEvent.ACTION_UP)
            textView_touchInfo.setText("");
        return super.dispatchTouchEvent(event);
    }

    public int getType(MotionEvent event, int index) {
        float x = event.getX(index);
        int bits1 = Float.floatToIntBits(event.getToolMajor(index));
        float wx = (float) (bits1 & 0xff);
        float wy = (float) ((bits1 >> 8) & 0xff);

        //Normal Touch:1 SideTouch:2
        int type = 1;
        if (Math.round(wx) == 30 && (x == 0 || Math.round(x) >= Common.screen_W - 2 ||
                Math.round(x) <= Common.screen_W)) {
            type = 2;
        }
        return type;
    }

    public void update(String s) {
        Message msg = new Message();
        msg.obj = s;
        textHandler.sendMessage(msg);
    }

    class TextHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            textView_resultInfo.setText((String)msg.obj);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            boolean quit = drawingView.onBackPressed();
            if (!quit) {
                for (Button btn:btns)
                    btn.setVisibility(View.VISIBLE);
                textView_touchInfo.setVisibility(View.VISIBLE);
                surfaceView.setVisibility(View.INVISIBLE);
                return false;
            }
            else
                return super.onKeyDown(keyCode, event);
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_debug) {
                debug_info ^= true;
                return;
            }
            if (v.getId() == R.id.btn_lockScreen) {
                Toast.makeText(MainActivity.this, "锁屏Demo", Toast.LENGTH_SHORT).show();
                drawingView.lockScreenDemo.changeDisplay(true);
            } else if (v.getId() == R.id.btn_camera) {
                Toast.makeText(MainActivity.this, "相机Demo", Toast.LENGTH_SHORT).show();
                drawingView.cameraDemo.changeDisplay(true);
                surfaceView.setVisibility(View.VISIBLE);
            } else if (v.getId() == R.id.btn_clock) {
                Toast.makeText(MainActivity.this, "闹铃Demo", Toast.LENGTH_SHORT).show();
                drawingView.clockDemo.changeDisplay(true);
            } else if (v.getId() == R.id.btn_movingScreen) {
                Toast.makeText(MainActivity.this,"移屏Demo", Toast.LENGTH_SHORT).show();
                drawingView.movingScreenDemo.changeDisplay(true);
            }
            for (Button btn:btns)
                btn.setVisibility(View.INVISIBLE);
            textView_touchInfo.setVisibility(View.INVISIBLE);
        }
    };
}