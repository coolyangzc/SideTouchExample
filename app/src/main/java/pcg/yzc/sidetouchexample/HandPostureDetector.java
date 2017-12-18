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
        if (countL > 0 && countR == 0)
            confL += 1;
        else if (countR > 0 && countL == 0)
            confR += 1;
        if (highestL >= 0 && highestR >= 0)
            if (highestL <= 6 && highestR > 7)
                confL += 1;
            else if (highestR <= 6 && highestL > 7)
                confR += 1;
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
        return res;
    }
}
