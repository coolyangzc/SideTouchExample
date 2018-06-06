package pcg.yzc.sidetouchexample;

import android.os.SystemClock;

public class HandPostureDetector {
    //int[][] capa = new int[6][Common.CapaNum_H]; //0~2: Left; 3~5: Right; 0~29: Up~Down
    final int L = 0, R = 5;

    public HistoryValueContainer confidenceL = new HistoryValueContainer(2000, 0.01);
    public HistoryValueContainer confidenceR = new HistoryValueContainer(2000, 0.01);
    public HistoryValueContainer confidenceU = new HistoryValueContainer(1000, 0.01);
    public HistoryValueContainer confidenceD = new HistoryValueContainer(1000, 0.01);

    private HandPostureResult res = new HandPostureResult();

    public HandPostureResult predict(int[][] capa) {

        double [] conf = new double[6];
        double confL = 0, confR = 0;
        double gravity = 0, force = 0;
        int countL = 0, countR = 0, highestL = -1, highestR = -1;
        for (int j = 0; j < Common.CapaNum_H; j++) {
            if (capa[L][j] > 10) {
                countL++;
                if (highestL == -1)
                    highestL = j;
                gravity += j * capa[L][j];
                force += capa[L][j];
            }
            if (capa[R][j] > 10) {
                countR++;
                if (highestR == -1)
                    highestR = j;
                gravity += j * capa[R][j];
                force += capa[R][j];
            }
        }

        boolean discrete = false;
        int discrete_L = 0, discrete_R = 0;
        for (int j = 0; j < Common.CapaNum_H; j++)
            if (capa[L][j] > 10) {
                if (!discrete) {
                    discrete = true;
                    discrete_L++;
                }
            } else
                discrete = false;
        discrete = false;
        for (int j = 0; j < Common.CapaNum_H; j++)
            if (capa[R][j] > 10) {
                if (!discrete) {
                    discrete = true;
                    discrete_R++;
                }
            } else
                discrete = false;

        if (countL > 0 && countR == 0)
            confL += 1;
        else if (countR > 0 && countL == 0)
            confR += 1;
        if (highestL >= 0 && highestR >= 0)
            if (highestL <= 6 && highestR > 7)
                confL += 1;
            else if (highestR <= 6 && highestL > 7)
                confR += 1;
        if (discrete_L >= 3 && discrete_R < 3)
            confR += 1;
        else if (discrete_R >= 3 && discrete_L < 3)
            confL += 1;
        else if (discrete_L >= discrete_R + 2)
            confR += 1;
        else if (discrete_R >= discrete_L + 2)
            confL += 1;
        long curTime = SystemClock.uptimeMillis();
        confidenceL.update(confL, curTime);
        confidenceR.update(confR, curTime);

        if (force <= 10) {
            confidenceU.update(0, curTime);
            confidenceD.update(0, curTime);
        } else {
            gravity /= force;
            if (gravity < Common.CapaNum_H / 2) {
                confidenceU.update(1, curTime);
                confidenceD.update(0, curTime);
            } else {
                confidenceU.update(0, curTime);
                confidenceD.update(1, curTime);
            }
        }

        res.L = confidenceL.getValue();
        res.R = confidenceR.getValue();
        res.U = confidenceU.getValue();
        res.D = confidenceD.getValue();
        res.discrete_L = discrete_L;
        res.discrete_R = discrete_R;

        if (countL >= 5 && countR >= 5 && force >= 500)
            res.grip = true;
        else
            res.grip = false;
        return res;
    }
}
