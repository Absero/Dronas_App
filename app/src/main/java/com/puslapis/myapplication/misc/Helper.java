package com.puslapis.myapplication.misc;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Helper {
    static public double[] Pole2XY(double r, double angle) {
        return new double[]{r / 100 * cos(angle * Math.PI / 180), r / 100 * sin(angle * Math.PI / 180)};
    }


    int getCRC(int[] message) {
        int crc = 0;

        for (int value : message) {
            crc = (crc ^ value) & 0xff;
            for (int j = 0; j < 8; j++) {
                if ((crc & 1) == 1)
                    crc = (crc ^ 0x91) & 0xff;      //CRC7_POLY = 0x91
                crc >>= 1;
            }
        }
        return crc;
    }
}
