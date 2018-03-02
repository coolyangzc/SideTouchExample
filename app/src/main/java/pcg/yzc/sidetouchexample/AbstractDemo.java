package pcg.yzc.sidetouchexample;

import android.graphics.Canvas;

public abstract class AbstractDemo {
    protected boolean display;
    protected HandPostureResult res;

    public AbstractDemo() {
        display = false;
    }

    public void changeDisplay(boolean display_) {
        display = display_;
    }

    public boolean isDisplay() {
        return display;
    }

    public void update(HandPostureResult res_) {
        res = res_;
    }

    public abstract void draw(Canvas canvas);
}
