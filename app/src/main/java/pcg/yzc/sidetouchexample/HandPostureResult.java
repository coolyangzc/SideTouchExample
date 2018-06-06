package pcg.yzc.sidetouchexample;

public class HandPostureResult {
    public double L, R, U, D;
    public double[] highest_gravity = new double[2];
    public boolean grip;
    //Debug Features
    public int discrete_L, discrete_R, lowest_L, lowest_R;
}
