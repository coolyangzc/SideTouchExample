package pcg.yzc.sidetouchexample;

import android.os.SystemClock;

public class HandPostureDetector {
    //int[][] capa = new int[6][Common.CapaNum_H]; //0~2: Left; 3~5: Right; 0~29: Up~Down
    final int L = 0, R = 5, INF = 100;
    final int[] D = {L, R};

    public HistoryValueContainer confidenceL = new HistoryValueContainer(1500, 0.01);
    public HistoryValueContainer confidenceR = new HistoryValueContainer(1500, 0.01);
    public HistoryValueContainer confidenceU = new HistoryValueContainer(1000, 0.01);
    public HistoryValueContainer confidenceD = new HistoryValueContainer(1000, 0.01);

    private HandPostureResult res = new HandPostureResult();

    private double[] conf = new double[6], highest_gravity = new double[6];
    private int[] count = new int[6], highest = new int[6],
            discrete = new int[6], low_discrete = new int[6],
            lowest_len = new int[6];
    private boolean[] connected = new boolean[6];
    public HandPostureResult predict(int[][] capa) {

        for(int i : D) {
            conf[i] = 0;
            count[i] = 0;
            low_discrete[i] = discrete[i] = 0;
            highest[i] = INF;
            lowest_len[i] = -1;
            connected[i] = false;
        }
        double gravity = 0, force = 0;

        for(int i : D) {
            int up = -1;
            for (int j = 0; j < Common.CapaNum_H; j++) {
                if (capa[i][j] > 10) {
                    count[i]++;
                    if (highest[i] == INF)
                        highest[i] = j;
                    if (!connected[i]) {
                        connected[i] = true;
                        discrete[i]++;
                        up = j;
                        if (j >= 10)
                            low_discrete[i]++;
                    }
                    lowest_len[i] = j - up + 1;
                    gravity += j * capa[i][j];
                    force += capa[i][j];
                } else {
                    connected[i] = false;
                    up = -1;
                }
            }
        }

        for(int i : D) {
            double high_g = 0, high_f = 0;
            boolean begin = false;
            for (int j = 0; j < Common.CapaNum_H; j++) {
                if (capa[i][j] > 10) {
                    begin = true;
                    high_g += j * capa[i][j];
                    high_f += capa[i][j];
                } else {
                    if (j <= 1)
                        high_g = high_f = 0;
                    else if (begin)
                        break;
                }
            }
            if (high_f >= 20)
                highest_gravity[i] = high_g / high_f;
            else
                highest_gravity[i] = -1;
        }
        res.highest_gravity[0] = highest_gravity[L];
        res.highest_gravity[1] = highest_gravity[R];

        for (int p : D) {
            int q = 5 - p;
            if (count[p] > 0 && count[q] == 0)
                conf[p] += 0.5;
            if (highest[p] <= 7 && highest[q] > 8)
                conf[p]++;
            if (discrete[p] < 3 && discrete[q] >= 3)
                conf[p]++;
            if (low_discrete[p] + 2 <= low_discrete[q])
                conf[p]++;
            if (lowest_len[p] > 5 && lowest_len[q] <= 5)
                conf[p]++;
            else if (lowest_len[p] == -1 && lowest_len[q] != -1 && lowest_len[q] <= 4)
                conf[p]++;
        }

        long curTime = SystemClock.uptimeMillis();
        confidenceL.update(conf[L], curTime);
        confidenceR.update(conf[R], curTime);

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
        res.discrete_L = discrete[L];
        res.discrete_R = discrete[R];
        res.lowest_L = lowest_len[L];
        res.lowest_R = lowest_len[R];

        if (count[L] >= 5 && count[R] >= 5 && force >= 500)
            res.grip = true;
        else
            res.grip = false;
        return res;
    }
}
