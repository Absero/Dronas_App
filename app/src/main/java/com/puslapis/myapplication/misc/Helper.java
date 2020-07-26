package com.puslapis.myapplication.misc;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Helper {
    static public double[] Pole2XY(double r, double angle){
        return new double[]{r/100*cos(angle*Math.PI/180), r/100*sin(angle*Math.PI/180)};
    }
}
