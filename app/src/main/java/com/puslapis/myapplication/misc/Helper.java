package com.puslapis.myapplication.misc;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Helper {
    private static final String TAG = "Main -> helper";

    static public double[] Pole2XY(double r, double angle) {
        return new double[]{r / 100 * cos(angle * Math.PI / 180), r / 100 * sin(angle * Math.PI / 180)};
    }

    static public byte getCRC(byte[] message, int length) {
        int crc = 0;

        for (int i = 0; i < length; i++) {
            crc = (crc ^ message[i]) & 0xff;
            for (int j = 0; j < 8; j++) {
                if ((crc & 1) != 0)
                    crc ^= 0x91;
                crc >>= 1;
            }
        }
        return (byte) crc;
    }
}
