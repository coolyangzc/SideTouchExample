package pcg.yzc.sidetouchexample;

import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    DrawingView drawingView;
    static TextView textView_touchInfo, textView_resultInfo;
    public TextHandler textHandler = new TextHandler();

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
        findViewById(R.id.btn_lockScreen).setOnClickListener(onClickLockScreenListener);

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

    private View.OnClickListener onClickLockScreenListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "锁屏Demo", Toast.LENGTH_LONG).show();
            drawingView.lockScreenDemo.changeDisplay();
        }
    };
}
