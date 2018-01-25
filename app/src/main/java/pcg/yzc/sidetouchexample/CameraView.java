package pcg.yzc.sidetouchexample;

import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraView implements SurfaceHolder.Callback{
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    Camera mCamera;

    CameraView(SurfaceView view_) {
        mSurfaceView = view_;
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);

            //设置预览偏移90度,一般的设备都是90，但某些设备会偏移180
            mCamera.setDisplayOrientation(90);

            mCamera.startPreview();
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //停止预览并释放camera对象并置为null
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }
}