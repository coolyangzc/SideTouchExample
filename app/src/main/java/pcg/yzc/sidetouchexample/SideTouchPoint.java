package pcg.yzc.sidetouchexample;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by coolyangzc on 2017/10/30.
 */

public class SideTouchPoint implements Parcelable {

    public static final int LEFT = 1;
    public static final int RIGHT = 0;

    private int barId;
    private int trkId;
    private int forceLvl;
    private float center;
    private float bottom;
    private float top;
    private long time;


    public SideTouchPoint(int barId, int trkId, int forceLvl, float center, float bottom, float top, long time) {
        this.barId = barId;
        this.trkId = trkId;
        this.forceLvl = forceLvl;
        this.center = center;
        this.bottom = bottom;
        this.top = top;
        this.time = time;
    }

    public SideTouchPoint(Parcel in) {
        barId = in.readInt();
        trkId = in.readInt();
        forceLvl = in.readInt();
        center = in.readFloat();
        bottom = in.readFloat();
        top = in.readFloat();
        time = in.readLong();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(barId);
        dest.writeInt(trkId);
        dest.writeInt(forceLvl);
        dest.writeFloat(center);
        dest.writeFloat(bottom);
        dest.writeFloat(top);
        dest.writeLong(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SideTouchPoint> CREATOR = new Creator<SideTouchPoint>() {
        @Override
        public SideTouchPoint createFromParcel(Parcel source) {
            return new SideTouchPoint(source);
        }

        @Override
        public SideTouchPoint[] newArray(int size) {
            return new SideTouchPoint[size];
        }
    };

    public int getBarId() {
        return barId;
    }

    public void setBarId(int barId) {
        this.barId = barId;
    }

    public int getTrkId() {
        return trkId;
    }

    public void setTrkId(int trkId) {
        this.trkId = trkId;
    }

    public int getForceLvl() {
        return forceLvl;
    }

    public void setForceLvl(int forceLvl) {
        this.forceLvl = forceLvl;
    }

    public float getCenter() {
        return center;
    }

    public void setCenter(float center) {
        this.center = center;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
