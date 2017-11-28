package pcg.yzc.sidetouchexample;

public class HandPostureDetector {
    //int[][] capa = new int[6][Common.CapaNum_H]; //0~2: Left; 3~5: Right; 0~29: Up~Down
    final int L = 0, R = 5;
    public double predict(int[][] capa) {
        double confL = 0, confR = 0;
        int countL = 0, countR = 0, highestL = -1, highestR = -1;
        for (int j = 0; j < Common.CapaNum_H; j++) {
            if (capa[L][j] > 10) {
                countL++;
                if (highestL == -1)
                    highestL = j;
            }
            if (capa[R][j] > 10) {
                countR++;
                if (highestR == -1)
                    highestR = j;
            }
        }
        if (countL * countR == 0)
            if (countR == 0)
                confL += 1;
            else
                confR += 1;
        if (highestL >= 0 && highestR >= 0)
            if (highestL <= 5 && highestR > 6)
                confL += 1;
            else if (highestR <= 5 && highestL > 6)
                confR += 1;
        if (confL > confR)
            return -1;
        if (confL < confR)
            return 1;
        return 0;
    }
}
